package com.baby.care.service;

import com.baby.care.controller.request.SaveParentRequest;
import com.baby.care.errors.AppUserNotFoundException;
import com.baby.care.errors.FailedToSaveParentException;
import com.baby.care.model.AppUser;
import com.baby.care.model.Parent;
import com.baby.care.model.Token;
import com.baby.care.model.enums.Sex;
import com.baby.care.repository.AppUserRepository;
import com.baby.care.repository.ParentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParentServiceTest {
    @InjectMocks
    private ParentService parentService;
    @Mock
    private AppUserService appUserService;
    @Mock
    private ParentRepository parentRepository;
    @Mock
    private AppUserRepository appUserRepository;

    @Test
    void testParentSavedSuccessfully()
    {
        AppUser appUser = new AppUser(1L, "user@gmail.com", "password", true, true, true, true, null, List.of(new Token()));

        Parent parent = Parent.builder()
                .name("Name")
                .dateOfBirth(LocalDate.of(2023, Month.OCTOBER, 12))
                .sex(Sex.FEMALE)
                .location("Romania")
                .appUser(appUser)
                .build();

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));
        when(parentRepository.save(any(Parent.class))).thenReturn(parent);
        when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);

        SaveParentRequest request = new SaveParentRequest("Name", LocalDate.of(2023, Month.OCTOBER, 12), Sex.FEMALE, "Romania");
        parentService.saveParent(request, "token");

        verify(appUserService, times(1)).findCurrentAppUser(anyString());
        verify(parentRepository, times(1)).save(any(Parent.class));
        verify(appUserRepository, times(1)).save(any(AppUser.class));

        assertEquals("Name", parent.getName());
        assertEquals(appUser.getUsername(), parent.getAppUser().getUsername());
        assertEquals(parent.getName(), appUser.getParent().getName());
        assertNotNull(appUser.getParent().getAge());
    }

    @Test
    void testParentNotSaved_UserNotFound() {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.empty());

        SaveParentRequest request = new SaveParentRequest("Name", LocalDate.of(2023, Month.OCTOBER, 12), Sex.FEMALE, "Romania");

        assertThrows(AppUserNotFoundException.class, () -> parentService.saveParent(request, "token"));

    }

    @Test
    void testParentNotSaved_ParentAlreadyExists() {
        AppUser appUser = new AppUser(1L, "user@gmail.com", "password", true, true, true, true, new Parent(), List.of(new Token()));

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));

        SaveParentRequest request = new SaveParentRequest("Name", LocalDate.of(2023, Month.OCTOBER, 12), Sex.FEMALE, "Romania");

        assertThrows(FailedToSaveParentException.class, () -> parentService.saveParent(request, "token"));

    }

    @Test
    void testParentNotSaved() {
        AppUser appUser = new AppUser(1L, "user@gmail.com", "password", true, true, true, true, null, List.of(new Token()));

        Parent parent = Parent.builder()
                .name("Name")
                .age(30)
                .sex(Sex.FEMALE)
                .location("Romania")
                .appUser(appUser)
                .build();

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));
        when(parentRepository.save(any(Parent.class))).thenReturn(null);

        SaveParentRequest request = new SaveParentRequest("Name", LocalDate.of(2023, Month.OCTOBER, 12), Sex.FEMALE, "Romania");

        assertThrows(FailedToSaveParentException.class, () -> parentService.saveParent(request, "token"));
    }
}