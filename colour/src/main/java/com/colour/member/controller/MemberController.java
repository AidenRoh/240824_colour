package com.colour.member.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member/test")
public class MemberController {

    @GetMapping("/new-member")
    public String newMember() {
        System.out.println("called");
        return "new-member";
    }

    @PostMapping("/edit-member")
    public String editMember() {
        System.out.println("called");
        return "edit-form";
    }

    @GetMapping("/get-members")
    public String getMembers() {
        System.out.println("called");
        return "get-members";
    }

    @DeleteMapping("/delete-member")
    public String deleteMember() {
        System.out.println("called");
        return "delete-form";
    }
}
