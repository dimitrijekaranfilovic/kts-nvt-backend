package com.ktsnvt.ktsnvt.controller;

import com.ktsnvt.ktsnvt.annotations.IsAdmin;
import com.ktsnvt.ktsnvt.config.jwt.JwtTokenUtil;
import com.ktsnvt.ktsnvt.dto.auth.AuthRequest;
import com.ktsnvt.ktsnvt.dto.auth.AuthResponse;
import com.ktsnvt.ktsnvt.dto.createesuperuser.CreateSuperUserRequest;
import com.ktsnvt.ktsnvt.dto.createesuperuser.CreateSuperUserResponse;
import com.ktsnvt.ktsnvt.dto.readsuperusers.ReadSuperUsersRequest;
import com.ktsnvt.ktsnvt.dto.readsuperusers.ReadSuperUsersResponse;
import com.ktsnvt.ktsnvt.dto.updatepassword.UpdatePasswordRequest;
import com.ktsnvt.ktsnvt.dto.updatesalary.UpdateSalaryRequest;
import com.ktsnvt.ktsnvt.dto.updatesuperuser.UpdateSuperUserRequest;
import com.ktsnvt.ktsnvt.exception.NotFoundException;
import com.ktsnvt.ktsnvt.model.Authority;
import com.ktsnvt.ktsnvt.model.Salary;
import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.service.SalaryService;
import com.ktsnvt.ktsnvt.service.SuperUserService;
import com.ktsnvt.ktsnvt.support.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/super-users")
public class SuperUserController {
    private final SuperUserService superUserService;
    private final SalaryService salaryService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil tokenUtil;

    private final EntityConverter<CreateSuperUserRequest, SuperUser> createSuperUserToSuperUser;
    private final EntityConverter<SuperUser, CreateSuperUserResponse> superUserToCreateSuperUserResponse;

    private final EntityConverter<SuperUser, ReadSuperUsersResponse> superUserToReadSuperUserResponse;

    private final EntityConverter<UpdateSalaryRequest, Salary> updateSalaryToSalary;

    @Autowired
    public SuperUserController(SuperUserService superUserService,
                               SalaryService salaryService,
                               AuthenticationManager authenticationManager, JwtTokenUtil tokenUtil, EntityConverter<CreateSuperUserRequest, SuperUser> createSuperUserToSuperUser,
                               EntityConverter<SuperUser, CreateSuperUserResponse> superUserToCreateSuperUserResponse,
                               EntityConverter<SuperUser, ReadSuperUsersResponse> superUserToReadSuperUserResponse,
                               EntityConverter<UpdateSalaryRequest, Salary> updateSalaryToSalary) {
        this.superUserService = superUserService;
        this.salaryService = salaryService;
        this.authenticationManager = authenticationManager;
        this.tokenUtil = tokenUtil;
        this.createSuperUserToSuperUser = createSuperUserToSuperUser;
        this.superUserToCreateSuperUserResponse = superUserToCreateSuperUserResponse;
        this.superUserToReadSuperUserResponse = superUserToReadSuperUserResponse;
        this.updateSalaryToSalary = updateSalaryToSalary;
    }

    @PreAuthorize("hasAnyAuthority({'ADMIN', 'MANAGER'}) and #id == authentication.principal.id")
    @PutMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@PathVariable Integer id, @RequestBody @Valid UpdatePasswordRequest request) {
        superUserService.updatePassword(id, request.getOldPassword(), request.getNewPassword());
    }

    @PreAuthorize("hasAnyAuthority({'ADMIN', 'MANAGER'}) and #id == authentication.principal.id")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSuperUser(@PathVariable Integer id, @RequestBody @Valid UpdateSuperUserRequest request) {
        superUserService.update(id, request.getName(), request.getSurname(), request.getEmail());
    }

    @IsAdmin
    @PutMapping("/{id}/salary")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSalary(@PathVariable Integer id, @RequestBody @Valid UpdateSalaryRequest request) {
        // This will throw if the id is not an id of super user -> prevent updating employee's salary on this endpoint
        superUserService.read(id);
        salaryService.updateUserSalary(id, updateSalaryToSalary.convert(request));
    }

    @IsAdmin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateSuperUserResponse createSuperUser(@RequestBody @Valid CreateSuperUserRequest request) {
        var superUser = createSuperUserToSuperUser.convert(request);
        var result = superUserService.create(superUser);
        return superUserToCreateSuperUserResponse.convert(result);
    }

    @IsAdmin
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ReadSuperUsersResponse> readSuperUsers(ReadSuperUsersRequest request, @PageableDefault Pageable pageable) {
        var page = superUserService.read(request.getQuery(), request.getSalaryLowerBound(), request.getSalaryUpperBound(), request.getType(), pageable);
        return page.map(superUserToReadSuperUserResponse::convert);
    }

    @IsAdmin
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteManager(@PathVariable Integer id) {
        superUserService.deleteManager(id);
    }

    @PostMapping(value = "/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse authenticate(@RequestBody @Valid AuthRequest request){
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenUtil.generateToken(authentication);
        String username = tokenUtil.extractUsernameFromToken(token);
        try {
            var user = this.superUserService.findByEmail(username);
            var authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            return new AuthResponse(user.getId(), user.getName(), user.getSurname(), token, authorities);
        } catch (Exception ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }
}
