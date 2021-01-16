package manfred.data.helper;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class StringSplitter {

    public static List<String> splitAtCommas(String line) {
        return Arrays.stream(line.split(","))
            .map(String::trim)
            .collect(toList());
    }

    public static List<List<String>> splitAtCommas(List<String> listOfString) {
        return listOfString.stream().map(StringSplitter::splitAtCommas).collect(toList());
    }
}
