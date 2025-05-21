package tn.esprit.innoxpert.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.DTO.SettingResponse;
import tn.esprit.innoxpert.Service.SettingService;

import java.util.List;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;

    @GetMapping
    public List<SettingResponse> getAllSettings() {
        return settingService.getAllSettings();
    }

    @PutMapping("/{key}")
    public ResponseEntity<String> updateSetting(@PathVariable String key, @RequestBody String value) {
        settingService.updateSetting(key, value);
        return ResponseEntity.ok("Setting Updated Successfully") ;
    }
}
