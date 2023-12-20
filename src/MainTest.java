import java.io.*;
import java.nio.charset.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class MainTest {

    @Test
    public void twoForOne() {
        int n = 2;
        int k = 1;
        List<House> houses = new ArrayList<>();
        houses.add(new House(0, 1, 1));
        houses.add(new House(1, 3, 3));
        Set<Store> expected = new HashSet<>();
        expected.add(new Store(2, 2));
        Assertions.assertEquals(expected, Main.donutTime(n, k, houses));
    }
}
