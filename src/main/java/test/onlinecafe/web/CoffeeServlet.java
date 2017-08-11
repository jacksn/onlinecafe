package test.onlinecafe.web;

import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import test.onlinecafe.config.AppConfiguration;
import test.onlinecafe.dto.CoffeeOrderDto;
import test.onlinecafe.dto.CoffeeOrderItemDto;
import test.onlinecafe.dto.Notification;
import test.onlinecafe.dto.NotificationType;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.service.CoffeeOrderService;
import test.onlinecafe.service.CoffeeTypeService;
import test.onlinecafe.service.ConfigurationService;
import test.onlinecafe.util.CoffeeOrderUtil;
import test.onlinecafe.util.discount.Discount;
import test.onlinecafe.util.discount.NoDiscount;
import test.onlinecafe.util.exception.NotFoundException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;
import static test.onlinecafe.web.CoffeeServlet.*;

@WebServlet({PATH_ROOT, PATH_ORDER, PATH_CANCEL})
public class CoffeeServlet extends HttpServlet {
    static final String PATH_ROOT = "/";
    static final String PATH_ORDER = "/order";
    static final String PATH_CANCEL = "/cancel";
    private static final String APP_PROPERTIES_FILE = "application.properties";
    private static final String JSP_ROOT = "WEB-INF/";
    private static final String PAGE_COFFEE_TYPES_LIST = JSP_ROOT + "index.jsp";
    private static final String PAGE_ORDER_DETAILS = JSP_ROOT + "order.jsp";
    private static final String MODEL_ATTR_LOCALE = "locale";
    private static final String MODEL_ATTR_ORDER = "orderDto";
    private static final String MODEL_ATTR_ORDER_NAME = "name";
    private static final String MODEL_ATTR_ORDER_ADDRESS = "address";
    private static final String MODEL_ATTR_COFFEE_TYPES = "coffeeTypes";
    private static final String MODEL_ATTR_NOTIFICATION = "notification";
    private static final String MODEL_ATTR_DISCOUNT_DESCRIPTION = "discountDescription";

    private static final Logger log = getLogger(CoffeeServlet.class);

    private static String defaultLanguage = "en";
    private static Set<String> supportedLanguages;

    private String discountDescriptionMessageKey;

    private ConfigurableApplicationContext springContext;
    private CoffeeTypeService coffeeTypeService;
    private CoffeeOrderService coffeeOrderService;
    private ResourceBundleMessageSource messageSource;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.debug("Servlet initialization - start");

        springContext = new AnnotationConfigApplicationContext(AppConfiguration.class);
        coffeeTypeService = springContext.getBean(CoffeeTypeService.class);
        coffeeOrderService = springContext.getBean(CoffeeOrderService.class);
        messageSource = springContext.getBean(ResourceBundleMessageSource.class);
        ConfigurationService configurationService = springContext.getBean(ConfigurationService.class);

        List<String> languages;
        String discountClassName;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(APP_PROPERTIES_FILE)) {
            Properties appProperties = new Properties();
            appProperties.load(inputStream);

            discountClassName = appProperties.getProperty("app.discount_class_name");
            languages = Arrays.asList(appProperties.getProperty("i18n.supported_languages").split(","));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ServletException(e);
        }

        supportedLanguages = new HashSet<>();
        if (!languages.isEmpty()) {
            defaultLanguage = languages.get(0);
            for (String language : languages) {
                ResourceBundle bundle = getResourceBundle(language);
                if (bundle != null) {
                    supportedLanguages.add(language);
                }
            }
            supportedLanguages = Collections.unmodifiableSet(supportedLanguages);
        } else {
            ResourceBundle b = getResourceBundle(defaultLanguage);
            if (b == null) {
                throw new ServletException("No message bundles found");
            }
            supportedLanguages = Collections.singleton(defaultLanguage);
        }
        Locale.setDefault(Locale.forLanguageTag(defaultLanguage));


        Discount discount = springContext.getBean(discountClassName, Discount.class);

        if (discount == null) {
            discount = new NoDiscount(configurationService);
        }
        discount.init();
        this.discountDescriptionMessageKey = "discount." + discount.getClass().getSimpleName() + ".description";
        CoffeeOrderUtil.setDiscount(discount);

        log.debug("Servlet initialization - end");
    }

    @Override
    public void destroy() {
        log.debug("Servlet deinitialization - start");
        springContext.close();
        super.destroy();
        log.debug("Servlet deinitialization - end");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getRequestURI();
        log.debug("GET request received: {}", action);
        HttpSession session = request.getSession(true);
        Locale locale = resolveLocale(request, session);

        Notification notification = (Notification) session.getAttribute(MODEL_ATTR_NOTIFICATION);
        if (notification != null) {
            session.removeAttribute(MODEL_ATTR_NOTIFICATION);
            request.setAttribute(MODEL_ATTR_NOTIFICATION, notification);
        }

        if (PATH_ROOT.equals(action)) {
            log.debug("Show main page");
            String discountDescription = CoffeeOrderUtil.getDiscount().getDescription(
                    messageSource.getMessage(discountDescriptionMessageKey, null, locale),
                    messageSource.getMessage("label.currency_symbol", null, locale));
            request.setAttribute(MODEL_ATTR_DISCOUNT_DESCRIPTION, discountDescription);
            request.setAttribute(MODEL_ATTR_COFFEE_TYPES, coffeeTypeService.getEnabled());
            request.getRequestDispatcher(PAGE_COFFEE_TYPES_LIST).forward(request, response);
            return;
        } else if (PATH_ORDER.equals(action)) {
            CoffeeOrderDto orderDto = (CoffeeOrderDto) session.getAttribute(MODEL_ATTR_ORDER);
            if (orderDto != null && orderDto.getOrderItems() != null && !orderDto.getOrderItems().isEmpty()) {
                request.getRequestDispatcher(PAGE_ORDER_DETAILS).forward(request, response);
                log.debug("Show order details page");
                return;
            } else {
                log.debug("Order is empty. Redirect to main page.");
                session.removeAttribute(MODEL_ATTR_ORDER);
                setNotificationSessionAttribute(session, NotificationType.ERROR, locale, "error.empty_order");
            }
        } else if (PATH_CANCEL.equals(action)) {
            removeSessionAttributes(session);
        }
        response.sendRedirect(PATH_ROOT);
    }

    private CoffeeOrderDto createCoffeeOrder(String[] typeIds, String[] typeQuantities) {
        if (typeIds != null && typeQuantities != null && typeIds.length == typeQuantities.length) {
            List<CoffeeOrderItemDto> orderItemDtoList = new ArrayList<>();
            double orderTotalCost = 0;
            for (int i = 0; i < typeIds.length; i++) {
                int quantity = Integer.parseInt(typeQuantities[i]);
                if (quantity > 0) {
                    try {
                        int id = Integer.parseInt(typeIds[i]);
                        CoffeeType type = coffeeTypeService.get(id);
                        double itemCost = quantity * type.getPrice();
                        double discountedItemCost = CoffeeOrderUtil.getDiscountedItemCost(quantity, type.getPrice());
                        boolean discounted = discountedItemCost < itemCost;
                        CoffeeOrderItemDto orderItemDto = new CoffeeOrderItemDto(type, quantity, discountedItemCost, discounted);
                        orderItemDtoList.add(orderItemDto);
                        orderTotalCost += discountedItemCost;
                    } catch (NumberFormatException | NotFoundException e) {
                        log.warn(e.getMessage());
                    }
                }
            }
            double deliveryCost = CoffeeOrderUtil.getDeliveryCost(orderTotalCost);
            orderTotalCost += deliveryCost;
            if (!orderItemDtoList.isEmpty()) {
                return new CoffeeOrderDto(orderItemDtoList, deliveryCost, orderTotalCost);
            }
        }
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getRequestURI();
        log.debug("POST request received: {}", action);
        HttpSession session = request.getSession(true);
        Locale locale = resolveLocale(request, session);

        if (PATH_ROOT.equals(action)) {
            String[] typeIds = request.getParameterValues("id");
            String[] typeQuantities = request.getParameterValues("quantity");
            CoffeeOrderDto orderDto = createCoffeeOrder(typeIds, typeQuantities);
            if (orderDto != null) {
                session.setAttribute(MODEL_ATTR_ORDER, orderDto);
                response.sendRedirect(PATH_ORDER);
                return;
            } else {
                setNotificationSessionAttribute(session, NotificationType.ERROR, locale, "error.empty_order");
            }
        } else if (PATH_ORDER.equals(action)) {
            String name = request.getParameter(MODEL_ATTR_ORDER_NAME);
            String address = request.getParameter(MODEL_ATTR_ORDER_ADDRESS);
            CoffeeOrderDto orderDto = (CoffeeOrderDto) session.getAttribute(MODEL_ATTR_ORDER);
            if (orderDto == null || orderDto.getOrderItems() == null || orderDto.getOrderItems().isEmpty()) {
                session.removeAttribute(MODEL_ATTR_ORDER);
                setNotificationSessionAttribute(session, NotificationType.ERROR, locale, "error.empty_order");
                response.sendRedirect(PATH_ROOT);
                return;
            }
            if (name != null && address != null && !address.isEmpty()) {
                orderDto.setName(name);
                orderDto.setDeliveryAddress(address);
                coffeeOrderService.save(orderDto);
                removeSessionAttributes(session);
                setNotificationSessionAttribute(session, NotificationType.SUCCESS, locale, "label.order_accepted");
            } else {
                setNotificationSessionAttribute(session, NotificationType.ERROR, locale, "error.empty_address");
                response.sendRedirect(PATH_ORDER);
                return;
            }
        }
        response.sendRedirect(PATH_ROOT);
    }

    private Locale resolveLocale(HttpServletRequest request, HttpSession session) {
        Locale locale = (Locale) session.getAttribute(MODEL_ATTR_LOCALE);
        String langParam = request.getParameter("lang");

        if (langParam == null && locale == null) {
            locale = Locale.forLanguageTag(defaultLanguage);
        } else {
            if (langParam != null && supportedLanguages.contains(langParam)) {
                locale = Locale.forLanguageTag(langParam);
            } else if (locale == null) {
                locale = Locale.forLanguageTag(defaultLanguage);
            }
        }
        session.setAttribute(MODEL_ATTR_LOCALE, locale);
        return locale;
    }

    private ResourceBundle getResourceBundle(String language) {
        try {
            return ResourceBundle.getBundle("messages.app", Locale.forLanguageTag(language));
        } catch (MissingResourceException e) {
            log.error("No resource bundle found for language \"{}\"", language);
        }
        return null;
    }

    private void removeSessionAttributes(HttpSession session) {
        session.removeAttribute(MODEL_ATTR_NOTIFICATION);
        session.removeAttribute(MODEL_ATTR_ORDER);
        session.removeAttribute(MODEL_ATTR_ORDER_NAME);
        session.removeAttribute(MODEL_ATTR_ORDER_ADDRESS);
    }

    private void setNotificationSessionAttribute(HttpSession session, NotificationType type, Locale locale, String messageKey) {
        session.setAttribute(MODEL_ATTR_NOTIFICATION, new Notification(type, messageSource.getMessage(messageKey, null, locale)));
    }
}
