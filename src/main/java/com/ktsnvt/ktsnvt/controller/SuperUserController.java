package com.ktsnvt.ktsnvt.controller;

import com.ktsnvt.ktsnvt.dto.createesuperuser.CreateSuperUserRequest;
import com.ktsnvt.ktsnvt.dto.createesuperuser.CreateSuperUserResponse;
import com.ktsnvt.ktsnvt.dto.readsuperusers.ReadSuperUsersRequest;
import com.ktsnvt.ktsnvt.dto.readsuperusers.ReadSuperUsersResponse;
import com.ktsnvt.ktsnvt.dto.updatepassword.UpdatePasswordRequest;
import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.service.SuperUserService;
import com.ktsnvt.ktsnvt.support.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/super-users")
public class SuperUserController {
    private final SuperUserService superUserService;

    private final EntityConverter<CreateSuperUserRequest, SuperUser> createSuperUserToSuperUser;
    private final EntityConverter<SuperUser, CreateSuperUserResponse> superUserToCreateSuperUserResponse;

    private final EntityConverter<SuperUser, ReadSuperUsersResponse> superUserToReadSuperUserResponse;

    @Autowired
    public SuperUserController(SuperUserService superUserService,
                               EntityConverter<CreateSuperUserRequest, SuperUser> createSuperUserToSuperUser,
                               EntityConverter<SuperUser, CreateSuperUserResponse> superUserToCreateSuperUserResponse, EntityConverter<SuperUser, ReadSuperUsersResponse> superUserToReadSuperUserResponse) {
        this.superUserService = superUserService;
        this.createSuperUserToSuperUser = createSuperUserToSuperUser;
        this.superUserToCreateSuperUserResponse = superUserToCreateSuperUserResponse;
        this.superUserToReadSuperUserResponse = superUserToReadSuperUserResponse;
    }

    // PRE AUTHORIZE (ADMIN, MANAGER)
    // OWNING USER
    @PutMapping("/{id}/update-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@PathVariable Integer id, @RequestBody @Valid UpdatePasswordRequest request) {
        superUserService.updatePassword(id, request.getOldPassword(), request.getNewPassword());
    }

    // PRE AUTHORIZE (ADMIN)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateSuperUserResponse createSuperUser(@RequestBody @Valid CreateSuperUserRequest request) {
        var superUser = createSuperUserToSuperUser.convert(request);
        var result = superUserService.create(superUser);
        return superUserToCreateSuperUserResponse.convert(result);
    }

    // PRE AUTHORIZE (ADMIN)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ReadSuperUsersResponse> readSuperUsers(ReadSuperUsersRequest request, @PageableDefault Pageable pageable) {
        var page = superUserService.read(request.getQuery(), request.getSalaryLowerBound(), request.getSalaryUpperBound(), request.getType(), pageable);
        return page.map(superUserToReadSuperUserResponse::convert);
    }

    // PRE AUTHORIZE (ADMIN)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteManager(@PathVariable Integer id) {
        superUserService.deleteManager(id);
    }
}
