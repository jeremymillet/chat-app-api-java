package com.chatapp.api.service;

import com.chatapp.api.entity.User;
import com.chatapp.api.repository.UserRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;



@Service
public class CloudinaryService {

    @Autowired
    private UserRepository userRepository;

    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        System.out.println("Cloudinary Config -> cloud_name: " + cloudName + ", api_key: " + apiKey);

        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    public User uploadImage(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String oldPublicId = user.getProfilePicturePublicId(); // Peut être null
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

        // Supprimer l'ancienne image
        if (oldPublicId != null) {
            cloudinary.uploader().destroy(oldPublicId, ObjectUtils.emptyMap());
        }

        user.setProfilePicture(uploadResult.get("url").toString());
        user.setProfilePicturePublicId(uploadResult.get("public_id").toString());

        return userRepository.save(user);
    }
}