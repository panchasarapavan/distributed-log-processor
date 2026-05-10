package info.laughingbuddha.utils;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.stereotype.Component;

@Component
public class UlidGenerator {

    public String generateUlid() {
        return UlidCreator.getUlid().toString();
    }

    public String generateMonotonicUlid() {
        return UlidCreator.getMonotonicUlid().toString();
    }

    public boolean isValid(String ulid){
        if (ulid == null || ulid.length() != 26) {
            return false;
        }

        try {
            Ulid.from(ulid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public long extractTimestamp(String ulid) {
        return Ulid.from(ulid).getTime();
    }
}
