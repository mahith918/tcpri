package org.tcpri.workspace.user;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {
    private final RegistrationService registration;

    public RegistrationController(RegistrationService registration) {
        this.registration = registration;
    }

    @GetMapping("/")
    public String home() { return "redirect:/dashboard"; }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") RegistrationForm form,
                           BindingResult result, Model model) {
        if (result.hasErrors()) return "register";
        try {
            registration.register(form);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("registrationError", ex.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String login() { return "login"; }
}
