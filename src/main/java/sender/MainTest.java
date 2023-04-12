package sender;

import entity.Country;
import entity.Location;
import geo.GeoServiceImpl;
import i18n.LocalizationServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    public String MOSCOW_IP = "172.0.32.11";
    public String russianText = "Добро пожаловать";

    public String NEW_YORK_IP = "96.44.183.149";
    public String englishText = "Welcome";

    @BeforeAll
    public static void beforeAllMethod() {
        System.out.println("BeforAll call");
    }

    @BeforeEach
    public void beforeEachMethod() {
        System.out.println("BeforeEach call");
    }

    @AfterEach
    public void afterEachMethod() {
        System.out.println("AfterEach call");
    }

    @AfterAll
    public static void afterAllMethod() {
        System.out.println("AfterEach call");
    }

    @Test
    void testMessageSenderImpl_RUSSIA() {
        GeoServiceImpl geoServiceImpl = Mockito.mock(GeoServiceImpl.class);
        LocalizationServiceImpl localizationServiceImpl = Mockito.mock(LocalizationServiceImpl.class);
        MessageSenderImpl messageSenderImpl = new MessageSenderImpl(geoServiceImpl, localizationServiceImpl);

        Map<String, String> mock = new HashMap<>();
        mock.put(MessageSenderImpl.IP_ADDRESS_HEADER, MOSCOW_IP);

        Mockito.when(geoServiceImpl.byIp(MOSCOW_IP))
                .thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));
        Mockito.when(localizationServiceImpl.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");

        String expected = russianText;
        String actual = messageSenderImpl.send(mock);
        assertEquals(expected, actual);
    }

    @Test
    void testMessageSenderImpl_USA() {
        GeoServiceImpl geoServiceImpl = Mockito.mock(GeoServiceImpl.class);
        LocalizationServiceImpl localizationServiceImpl = Mockito.mock(LocalizationServiceImpl.class);
        MessageSenderImpl messageSenderImpl = new MessageSenderImpl(geoServiceImpl, localizationServiceImpl);

        Map<String, String> mock = new HashMap<>();
        mock.put(MessageSenderImpl.IP_ADDRESS_HEADER, NEW_YORK_IP);

        Mockito.when(geoServiceImpl.byIp(NEW_YORK_IP))
                .thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));
        Mockito.when(localizationServiceImpl.locale(Country.USA))
                .thenReturn("Welcome");

        String expected = englishText;
        String actual = messageSenderImpl.send(mock);
        assertEquals(expected, actual);
    }

    @Test
    void testDeterminingGeolocationByIp() {
        GeoServiceImpl geoServiceImpl = new GeoServiceImpl();
        Location expected = new Location("New York", Country.USA, " 10th Avenue", 32);
        Location actual = geoServiceImpl.byIp(NEW_YORK_IP);
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getCountry(), actual.getCountry());
    }

    @Test
    void testReturnTextChecksUSA() {
        LocalizationServiceImpl localizationServiceImpl = new LocalizationServiceImpl();
        String expected = englishText;
        String actual = localizationServiceImpl.locale(Country.USA);        // Проверил работу метода public String locale(Country country).
        assertEquals(expected, actual);
    }

    @Test
    void testReturnTextChecksRUSSIA() {
        LocalizationServiceImpl localizationServiceImpl = new LocalizationServiceImpl();
        String expected = russianText;
        String actual = localizationServiceImpl.locale(Country.RUSSIA);     // Проверил работу метода public String locale(Country country).
        assertEquals(expected, actual);
    }
}