package com.baby.care.service;

import com.baby.care.controller.repsonse.GetParentResponse;
import com.baby.care.controller.repsonse.SaveParentResponse;
import com.baby.care.controller.request.SaveParentRequest;
import com.baby.care.controller.request.UpdateParentRequest;
import com.baby.care.errors.ParentNotFoundException;
import com.baby.care.model.AppUser;
import com.baby.care.model.Parent;
import com.baby.care.model.Token;
import com.baby.care.model.enums.Sex;
import com.baby.care.repository.AppUserRepository;
import com.baby.care.repository.ParentRepository;
import org.junit.jupiter.api.BeforeEach;
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

    private AppUser appUser = null;
    private Parent parent = null;
    @BeforeEach
    void setAppUserWithParent() {
        //set AppUser with Parent
         appUser = new AppUser(1L, "user@gmail.com", "password", true, true, true, true, null, List.of(new Token()));

         parent = Parent.builder()
                .id(100L)
                .name("Test name")
                .sex(Sex.FEMALE)
                .appUser(appUser)
                .build();

        appUser.setParent(parent);
    }


    @Test
    void testParentSavedSuccessfully()
    {
        appUser.setParent(null);

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));
        when(parentRepository.save(any(Parent.class))).thenReturn(parent);
        when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);

        SaveParentRequest request = new SaveParentRequest("Name", LocalDate.of(2023, Month.OCTOBER, 12), Sex.FEMALE, "Romania");
        parentService.saveParent(request, "token");

        verify(appUserService, times(1)).findCurrentAppUser(anyString());
        verify(parentRepository, times(1)).save(any(Parent.class));
        verify(appUserRepository, times(1)).save(any(AppUser.class));

        assertEquals("Test name", parent.getName());
        assertEquals(appUser.getUsername(), parent.getAppUser().getUsername());
        assertNotNull(appUser.getParent().getAge());
    }

    @Test
    void testParentNotSaved_UserNotFound() {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.empty());

        SaveParentRequest request = new SaveParentRequest("Name", LocalDate.of(2023, Month.OCTOBER, 12), Sex.FEMALE, "Romania");

        SaveParentResponse result = parentService.saveParent(request, "token");

        assertNull(result.getId());

    }

    @Test
    void testParentNotSaved_ParentAlreadyExists() {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));

        SaveParentRequest request = new SaveParentRequest("Name", LocalDate.of(2023, Month.OCTOBER, 12), Sex.FEMALE, "Romania");

        SaveParentResponse result = parentService.saveParent(request, "token");

        //assert
        assertEquals(100L, result.getId());
        assertEquals("Test name", result.getName());
    }

    @Test
    void testParentNotSavedInDatabase() {
        appUser.setParent(null);

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));
        when(parentRepository.save(any(Parent.class))).thenReturn(null);

        SaveParentRequest request = new SaveParentRequest("Name", LocalDate.of(2023, Month.OCTOBER, 12), Sex.FEMALE, "Romania");

        SaveParentResponse result = parentService.saveParent(request, "token");

        assertNull(result.getId());
    }

    @Test
    void testGetParentSuccessfully() {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));
        when(parentRepository.findById(anyLong())).thenReturn(Optional.of(parent));

        GetParentResponse result = parentService.getParent("token");

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Test name", result.getName());
    }

    @Test
    void testGetParentUnsuccessful_AppUserNotFound() {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.empty());

        GetParentResponse result = parentService.getParent("token");

        assertNotNull(result);
        assertNull(result.getId());
    }

    @Test
    void testGetParentUnsuccessful_ParentNotFound() {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));
        when(parentRepository.findById(anyLong())).thenReturn(Optional.empty());

        GetParentResponse result = parentService.getParent("token");

        assertNotNull(result);
        assertNull(result.getId());
    }

    @Test
    void testGetParentThrowsError() {
        when(appUserService.findCurrentAppUser(anyString())).thenThrow(new RuntimeException("Simulated exception"));

        assertThrows(ParentNotFoundException.class,  () -> parentService.getParent("token"));
    }

    @Test
    void testUpdateParentSuccessfully() {
        UpdateParentRequest updateParentRequest = UpdateParentRequest.builder()
                .name("Update name")
                .sex(Sex.MALE)
                .location("Romania")
                .build();

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));
        when( parentRepository.save(parent)).thenReturn(parent);

        parentService.updateParent(updateParentRequest, "token");

        assertEquals("Update name", parent.getName());
        assertEquals(Sex.MALE, parent.getSex());
        assertEquals("Romania", parent.getLocation());
    }

    @Test
    void testUpdateParentUnsuccessfully_AppUserNotFound() {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.empty());

        SaveParentResponse response = parentService.updateParent(new UpdateParentRequest(), "token");

        assertNull(response.getId());
    }

    @Test
    void testUpdateParentUnsuccessfully_ParentNotFound() {
        appUser.setParent(null);

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));

        SaveParentResponse response = parentService.updateParent(new UpdateParentRequest(), "token");

        assertNull(response.getId());
    }
}