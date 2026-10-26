package org.tcpri.workspace.post;

import org.tcpri.workspace.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final SocialPostRepository posts;
    private final UserRepository users;

    public PostController(SocialPostRepository posts, UserRepository users) {
        this.posts = posts; this.users = users;
    }

    @GetMapping
    public String list(Authentication auth, Model model) {
        boolean reviewer = auth.getAuthorities().stream().anyMatch(a ->
            a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));
        model.addAttribute("posts", reviewer ? posts.findAllByOrderByPlannedAtAsc()
            : posts.findByCreatedBy_EmailIgnoreCaseOrderByPlannedAtAsc(auth.getName()));
        model.addAttribute("canReview", reviewer);
        return "posts";
    }

    @GetMapping("/new")
    public String newPost(Model model) {
        model.addAttribute("form", new PostForm());
        model.addAttribute("platforms", SocialPlatform.values());
        return "post-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") PostForm form,
                         org.springframework.validation.BindingResult result,
                         Authentication auth, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("platforms", SocialPlatform.values());
            return "post-form";
        }
        var creator = users.findByEmailIgnoreCase(auth.getName()).orElseThrow();
        posts.save(new SocialPost(form.getTitle(), form.getCaption(), form.getPlatform(), form.getPlannedAt(), creator));
        flash.addFlashAttribute("message", "Draft saved.");
        return "redirect:/posts";
    }

    @PostMapping("/{id}/submit")
    public String submit(@PathVariable Long id, Authentication auth, RedirectAttributes flash) {
        SocialPost post = posts.findById(id).orElseThrow();
        if (!post.getCreatedBy().getEmail().equalsIgnoreCase(auth.getName())) {
            throw new org.springframework.security.access.AccessDeniedException("Not your post.");
        }
        if (post.getStatus() != PostStatus.DRAFT && post.getStatus() != PostStatus.REJECTED) {
            throw new IllegalStateException("Only drafts or rejected posts can be submitted.");
        }
        post.setStatus(PostStatus.SUBMITTED);
        posts.save(post);
        flash.addFlashAttribute("message", "Post submitted for review.");
        return "redirect:/posts";
    }

    @PostMapping("/{id}/review")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String review(@PathVariable Long id, @RequestParam boolean approve,
                         Authentication auth, RedirectAttributes flash) {
        SocialPost post = posts.findById(id).orElseThrow();
        if (post.getStatus() != PostStatus.SUBMITTED) {
            throw new IllegalStateException("Only submitted posts can be reviewed.");
        }
        var reviewer = users.findByEmailIgnoreCase(auth.getName()).orElseThrow();
        post.setApprovedBy(approve ? reviewer : null);
        post.setStatus(approve ? PostStatus.APPROVED : PostStatus.REJECTED);
        posts.save(post);
        flash.addFlashAttribute("message", approve ? "Post approved." : "Post returned for changes.");
        return "redirect:/posts";
    }
}
