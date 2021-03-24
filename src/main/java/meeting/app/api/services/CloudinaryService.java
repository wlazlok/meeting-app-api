package meeting.app.api.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.SingletonManager;
import lombok.extern.slf4j.Slf4j;
import meeting.app.api.exceptions.MeetingApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Service
public class CloudinaryService {

    private Cloudinary cloudinary;

    public CloudinaryService() {
         cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dfurufcqe",
                "api_key", "",
                "api_secret", ""
        ));

        SingletonManager manager = new SingletonManager();
        manager.setCloudinary(cloudinary);
    }

    public String upload(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String publicId = uploadResult.get("public_id").toString();

            return publicId;
        } catch (Exception e) {
            log.info("cloudinary.service.upload.exception " + e.getMessage());
            throw new MeetingApiException("msg.err.cloudinary.service.upload");
        }
    }
}
