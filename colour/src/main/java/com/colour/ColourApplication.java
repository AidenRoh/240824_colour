package com.colour;

import org.apache.tika.language.detect.LanguageDetector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.util.ServiceLoader;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class ColourApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColourApplication.class, args);

        ServiceLoader<LanguageDetector> loader = ServiceLoader.load(LanguageDetector.class);

        if (!loader.iterator().hasNext()) {
            System.out.println("❌ No LanguageDetector found! Check tika-langdetect dependency.");
        } else {
            System.out.println("✅ LanguageDetector is available.");
            for (LanguageDetector detector : loader) {
                System.out.println("Detected Detector: " + detector.getClass().getName());
            }
        }


    }

}
