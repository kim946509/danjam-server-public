package com.example.danjamserver.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserManagementFormController {

    @GetMapping("/api/admin/users/form")
    public String getAdminForm(HttpServletRequest request, HttpServletResponse response) {
        String access = request.getHeader("Access");
        System.out.println("access = " + access);
        response.setHeader("Access", access);
        return "adminForm";
    }
    @GetMapping("/login")
    public String getAdminLoginForm() {
        return "adminLoginForm";
    }
}
