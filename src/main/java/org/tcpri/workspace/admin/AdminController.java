package org.tcpri.workspace.admin;

import org.tcpri.workspace.user.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository users;

    public AdminController(UserRepository users) { this.users = users; }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("pendingUsers", users.findByStatusOrderByCreatedAtAsc(AccountStatus.PENDING));
        model.addAttribute("allUsers", users.findAllByOrderByCreatedAtDesc());
        return "admin-users";
    }

    @PostMapping("/users/{id}/approve")
    public String approve(@PathVariable Long id, RedirectAttributes flash) {
        AppUser user = users.findById(id).orElseThrow();
        user.setStatus(AccountStatus.APPROVED);
        users.save(user);
        flash.addFlashAttribute("message", "Account approved.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/reject")
    public String reject(@PathVariable Long id, RedirectAttributes flash) {
        AppUser user = users.findById(id).orElseThrow();
        user.setStatus(AccountStatus.REJECTED);
        users.save(user);
        flash.addFlashAttribute("message", "Account rejected.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(@PathVariable Long id, @RequestParam Role role,
                             RedirectAttributes flash) {
        AppUser user = users.findById(id).orElseThrow();
        // Prevent accidental demotion of the currently configured initial admin
        // through a self-service form; implement a stronger last-admin guard before production.
        user.setRole(role);
        users.save(user);
        flash.addFlashAttribute("message", "Role updated.");
        return "redirect:/admin/users";
    }
}
