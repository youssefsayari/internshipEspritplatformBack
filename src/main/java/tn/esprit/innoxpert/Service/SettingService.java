package tn.esprit.innoxpert.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.DTO.InternshipResponse;
import tn.esprit.innoxpert.DTO.SettingResponse;
import tn.esprit.innoxpert.Entity.Internship;
import tn.esprit.innoxpert.Entity.Setting;
import tn.esprit.innoxpert.Repository.SettingRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository settingRepository;

    public List<SettingResponse> getAllSettings() {
        List<Setting> settings = settingRepository.findAll();
        return settings.stream().map(this::mapToSettingResponse).toList();
    }

    private SettingResponse mapToSettingResponse(Setting setting) {
        return new SettingResponse(
                setting.getKey(),
                setting.getValue()
        );
    }

    public void updateSetting(String key, String value) {
        Setting setting = settingRepository.findByKey(key).orElseThrow(() -> new RuntimeException("Setting Not Found"));
        setting.setValue(value);
        settingRepository.save(setting);
    }
}
