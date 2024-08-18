package com.baby.care.service;

import com.baby.care.controller.repsonse.GetBabyResponse;
import com.baby.care.controller.repsonse.SaveBabyResponse;
import com.baby.care.controller.request.SaveBabyRequest;
import com.baby.care.controller.request.UpdateBabyRequest;
import com.baby.care.errors.AppUserNotFoundException;
import com.baby.care.errors.FailedToSaveBabyException;
import com.baby.care.model.AppUser;
import com.baby.care.model.Baby;
import com.baby.care.model.Parent;
import com.baby.care.model.Token;
import com.baby.care.model.enums.Sex;
import com.baby.care.model.enums.TypeOfBirth;
import com.baby.care.repository.BabyRepository;
import com.baby.care.repository.ParentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BabyServiceTest {
    @InjectMocks
    private BabyService babyService;
    @Mock
    private BabyRepository babyRepository;
    @Mock
    private AppUserService appUserService;
    @Mock
    private ParentRepository parentRepository;

    private  AppUser appUser = new AppUser();
    private Parent parent = new Parent();;
    private  Baby baby = new Baby();


    @BeforeEach
    void setAppUserWithParentWithBaby() {
        parent = new Parent();
        parent.setId(1L);
        appUser = new AppUser(1L, "user@gmail.com", "password", true, true, true, true, parent, List.of(new Token()));

        baby = Baby.builder()
                .id(100L)
                .name("Rio")
                .sex(Sex.MALE)
                .dateOfBirth(LocalDate.of(2021, Month.MARCH, 8))
                .weight(15.0)
                .height(50.3)
                .typeOfBirth(TypeOfBirth.OTHER)
                .parent(parent)
                .build();

        parent.setBabies(List.of(baby));
    }

    @Test
    void testBabySavedSuccessfully()
    {
        Parent parent = new Parent();
        parent.setId(1L);
        AppUser appUser = new AppUser(1L, "user@gmail.com", "password", true, true, true, true, parent, List.of(new Token()));

        Baby baby = Baby.builder()
                .name("Rio")
                .sex(Sex.MALE)
                .dateOfBirth(LocalDate.of(2021, Month.MARCH, 8))
                .weight(15.0)
                .height(50.3)
                .typeOfBirth(TypeOfBirth.OTHER)
                .parent(parent)
                .build();

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));
        when(babyRepository.save(any(Baby.class))).thenReturn(baby);
        when(parentRepository.save(any(Parent.class))).thenReturn(parent);

        SaveBabyRequest request = new SaveBabyRequest("Rio", LocalDate.of(2021, Month.MARCH, 8),
                Sex.MALE, 15.0, 50.3, TypeOfBirth.OTHER, 0.8, "N/A");

        SaveBabyResponse response = babyService.saveBaby(request, "token");

        verify(appUserService, times(1)).findCurrentAppUser("token");
        verify(babyRepository, times(1)).save(any(Baby.class));
        verify(parentRepository, times(1)).save(any(Parent.class));

        assertNotEquals("",response.getAge());
    }

    @Test
    void testBabyNotSaved_AppUserNotFound()
    {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.empty());

        SaveBabyResponse response = babyService.saveBaby(new SaveBabyRequest(), "token");

        assertNull(response.getId());
    }

    @Test
    void testBabyNotSaved_ParentNotFound()
    {
        appUser.setParent(null);

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));

        SaveBabyResponse response = babyService.saveBaby(new SaveBabyRequest(), "token");

        assertNotNull(response);
        assertNull(response.getId());
    }

    @Test
    void testGetAllBabiesSuccessfully() {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));

        List<GetBabyResponse> babyResponseList = babyService.getAllBabies("token");

        assertEquals(1, babyResponseList.size());
        assertEquals("Rio", babyResponseList.get(0).getName());
    }

    @Test
    void testGetAllBabiesSuccessfully_MultipleBabies() {
        //add two Babies to Parent
        Baby baby1 = Baby.builder()
                .name("Lilo").
                dateOfBirth(LocalDate.of(2021, Month.JANUARY, 8))
                .sex(Sex.FEMALE)
                .typeOfBirth(TypeOfBirth.OTHER)
                .parent(parent)
                .build();

        parent.setBabies(List.of(baby, baby1));

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));

        List<GetBabyResponse> babyResponseList = babyService.getAllBabies("token");

        verify(appUserService, times(1)).findCurrentAppUser(anyString());

        assertEquals(2, babyResponseList.size());
        assertEquals("Rio", babyResponseList.get(0).getName());
        assertEquals("Lilo", babyResponseList.get(1).getName());
    }

    @Test
    void testGetAllBabiesUnsuccessfully_AppUserNotFound() {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.empty());

        List<GetBabyResponse> babyResponseList = babyService.getAllBabies("token");

        verify(appUserService, times(1)).findCurrentAppUser(anyString());
        assertEquals(0, babyResponseList.size());
    }

    @Test
    void testGetAllBabiesUnsuccessfully_ParentNotFound() {
        appUser.setParent(null);

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));

        List<GetBabyResponse> babyResponseList = babyService.getAllBabies("token");

        verify(appUserService, times(1)).findCurrentAppUser(anyString());
        assertEquals(0, babyResponseList.size());
    }

    @Test
    void testGetBabySuccessfully() {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));
        when(babyRepository.findById(anyLong())).thenReturn(Optional.of(baby));

        GetBabyResponse response = babyService.getBaby(100L, "token");

        verify(appUserService, times(1)).findCurrentAppUser("token");
        verify(babyRepository, times(1)).findById(100L);

        assertEquals(100L, response.getId());
        assertEquals("Rio", response.getName());
    }

    @Test
    void testGetBabyUnsuccessfully_AppUserNotFound() {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.empty());

        GetBabyResponse response = babyService.getBaby(100L, "token");

        verify(appUserService, times(1)).findCurrentAppUser("token");
        verify(babyRepository, never()).findById(100L);

        assertNotNull(response);
        assertNull(response.getId());
    }

    @Test
    void testGetBabyUnsuccessfully_ParentNotFound() {
        appUser.setParent(null);
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));

        GetBabyResponse response = babyService.getBaby(100L, "token");

        verify(appUserService, times(1)).findCurrentAppUser("token");
        verify(babyRepository, never()).findById(100L);

        assertNotNull(response);
        assertNull(response.getId());
    }

    @Test
    void testGetBabyUnsuccessfully_ParentWithoutBabies() {
        parent.setBabies(null);
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));

        GetBabyResponse response = babyService.getBaby(100L, "token");

        verify(appUserService, times(1)).findCurrentAppUser("token");
        verify(babyRepository, never()).findById(100L); // never called because Parent has 0 Babies

        assertNotNull(response);
        assertNull(response.getId());
        assertNull(parent.getBabies());
    }

    @Test
    void testGetBabyUnsuccessfully_BabyNotFound() {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));
        when(babyRepository.findById(anyLong())).thenReturn(Optional.empty());

        GetBabyResponse response = babyService.getBaby(5L, "token");

        verify(appUserService, times(1)).findCurrentAppUser("token");
        verify(babyRepository, times(1)).findById(5L);

        assertNotNull(response);
        assertNull(response.getId());
        assertEquals(1, parent.getBabies().size()); // Parent has a Baby, but with another id
    }

    @Test
    void testUpdateBabySuccessfully() {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));
        when(babyRepository.findById(anyLong())).thenReturn(Optional.of(baby));
        when(babyRepository.save(any(Baby.class))).thenReturn(baby);

        UpdateBabyRequest updateBabyRequest = new UpdateBabyRequest(100L, "Rio the Dog", LocalDate.of(2021, Month.MARCH, 8),
                Sex.MALE, 15.0, 50.0, TypeOfBirth.OTHER, 0.8, "update request");

        GetBabyResponse response = babyService.updateBaby(updateBabyRequest, "token");

        verify(appUserService, times(1)).findCurrentAppUser("token");
        verify(babyRepository, times(1)).findById(100L);
        verify(babyRepository, times(1)).save(baby);

        assertEquals("Rio the Dog", baby.getName());
        assertEquals("update request", baby.getComments());
    }

    @Test
    void testUpdateBabyUnsuccessfully_AppUserNotFound() {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.empty());

        GetBabyResponse response = babyService.updateBaby(new UpdateBabyRequest(), "token");

        verify(appUserService, times(1)).findCurrentAppUser("token");
        verify(babyRepository, never()).findById(anyLong());
        verify(babyRepository, never()).save(any(Baby.class));

        assertNotNull(response);
        assertNull(response.getId());
    }

    @Test
    void testUpdateBabyUnsuccessfully_ParentNotFound() {
        appUser.setParent(null);

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));

        GetBabyResponse response = babyService.updateBaby(new UpdateBabyRequest(), "token");

        verify(appUserService, times(1)).findCurrentAppUser("token");
        verify(babyRepository, never()).findById(anyLong());
        verify(babyRepository, never()).save(any(Baby.class));

        assertNotNull(response);
        assertNull(response.getId());
    }

    @Test
    void testUpdateBabyUnsuccessfully_ParentWithoutBabies() {
        parent.setBabies(null);

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));

        GetBabyResponse response = babyService.updateBaby(new UpdateBabyRequest(), "token");

        verify(appUserService, times(1)).findCurrentAppUser("token");
        verify(babyRepository, never()).findById(anyLong());
        verify(babyRepository, never()).save(any(Baby.class));

        assertNotNull(response);
        assertNull(response.getId());
    }

    @Test
    void testUpdateBabyUnsuccessfully_BabyNotFound() {
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(Optional.of(appUser));
        when(babyRepository.findById(anyLong())).thenReturn(Optional.empty());

        UpdateBabyRequest updateBabyRequest = new UpdateBabyRequest(5L, "Lila", LocalDate.of(2021, Month.JANUARY, 8),
                Sex.FEMALE, 15.0, 50.0, TypeOfBirth.OTHER, 0.8, "");

        GetBabyResponse response = babyService.updateBaby(updateBabyRequest, "token");

        verify(appUserService, times(1)).findCurrentAppUser("token");
        verify(babyRepository, times(1)).findById(5L);
        verify(babyRepository, never()).save(any(Baby.class));

        assertNotNull(response);
        assertNull(response.getId());
    }
}