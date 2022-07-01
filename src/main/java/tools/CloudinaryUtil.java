package tools;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Component;
@Component
public class CloudinaryUtil {
    private static Cloudinary cloudinary;
    public static Cloudinary getCloudinary() {
        if (cloudinary == null) {
            try {
                cloudinary = new Cloudinary("cloudinary://972125473677945:D1zx9AOK0PIan87hUlANFm5B8dU@ds85e9ucq");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cloudinary;
    }
}
