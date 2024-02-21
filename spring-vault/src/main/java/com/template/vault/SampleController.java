package com.template.vault;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.vault.config.VaultConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SampleController {

    private final VaultConfig vaultConfig;

    private final ObjectMapper objectMapper;

    @GetMapping(value = "/test")
    public String getVaultData() throws JsonProcessingException {
        return String.format("Data from vault : %s", objectMapper.writeValueAsString(vaultConfig));
    }
}
