package com.example.letscode.controllers;

import com.example.letscode.models.Message;
import com.example.letscode.models.User;
import com.example.letscode.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class MainController {
    @Autowired
    private MessageRepository messageRepo;
    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("mainCards")
    public String main(@RequestParam(required = false, defaultValue = "") String filter,
                       Model model) {
        var messages = filter == null || filter.isEmpty()
                ? messageRepo.findAll()
                : messageRepo.findByTagContains(filter);
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "mainCards";
    }

    @PostMapping("mainCards")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String text,
                      @RequestParam String tag,
                      @RequestParam("file") MultipartFile file,
                      Model model) throws IOException {
        Message message = new Message(text, tag, user);
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "_" + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            message.setFilename(resultFilename);
        }
        messageRepo.save(message);
        var messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "mainCards";
    }

}
