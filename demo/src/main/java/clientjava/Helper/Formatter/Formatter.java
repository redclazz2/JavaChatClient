package clientjava.Helper.Formatter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Formatter {

    public static <T> T Deserialize(byte[] data, Class<T> clss) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String decodedString = new String(data, StandardCharsets.UTF_8);
            return objectMapper.readValue(decodedString, clss);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T Deserialize(String data, Class<T> clss) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(data, clss);
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] Serialize(Object data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(data);
            return jsonString.getBytes("UTF-8");
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public static <T> T CastDataToType(Object data, Class<T> type) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(data, type);
    }
}
