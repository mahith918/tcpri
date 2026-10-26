package org.tcpri.workspace.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PasswordResetController {
    private final PasswordResetService resetService;
    public PasswordResetController(PasswordResetService resetService) { this.resetService = resetService; }
    @GetMapping("/forgot-password") public String forgotForm() { return "forgot-password"; }
    @PostMapping("/forgot-password") public String forgot(@RequestParam String email) { resetService.request(email); return "redirect:/forgot-password?sent"; }
    @GetMapping("/reset-password") public String resetForm(@RequestParam(required = false) String token, Model model) { model.addAttribute("token", token == null ? "" : token); return "reset-password"; }
    @PostMapping("/reset-password") public String reset(@RequestParam String token, @RequestParam String password, @RequestParam String confirmPassword, Model model) {
        if (!password.equals(confirmPassword) || !resetService.reset(token, password)) { model.addAttribute("token", token); model.addAttribute("error", "The link is invalid or expired, or the passwords do not match. Passwords must be at least 12 characters."); return "reset-password"; }
        return "redirect:/login?reset";
    }
}
