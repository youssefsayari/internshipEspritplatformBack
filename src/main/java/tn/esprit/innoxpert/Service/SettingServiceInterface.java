package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.DTO.SettingResponse;

import java.util.List;

public interface SettingServiceInterface {
    List<SettingResponse> getAllSettings();
    void updateSetting(String key, String value);
}
