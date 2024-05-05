package com.baby.care.service;

import com.baby.care.controller.repsonse.SaveBabyResponse;
import com.baby.care.controller.request.SaveBabyRequest;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(appUser);
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
        when(appUserService.findCurrentAppUser(anyString())).thenReturn(new AppUser());

        SaveBabyRequest request = new SaveBabyRequest("Rio", LocalDate.of(2021, Month.MARCH, 8),
                Sex.MALE, 15.0, 50.3, TypeOfBirth.OTHER, 0.8, "N/A");

        assertThrows(AppUserNotFoundException.class, () -> babyService.saveBaby(request, "token"));
    }

    @Test
    void testBabyNotSaved_ParentNotFound()
    {
        AppUser appUser = new AppUser(1L, "user@gmail.com", "password", true, true, true, true, null, List.of(new Token()));

        when(appUserService.findCurrentAppUser(anyString())).thenReturn(appUser);

        SaveBabyRequest request = new SaveBabyRequest("Rio", LocalDate.of(2021, Month.MARCH, 8),
                Sex.MALE, 15.0, 50.3, TypeOfBirth.OTHER, 0.8, "N/A");

        assertThrows(FailedToSaveBabyException.class, () -> babyService.saveBaby(request, "token"));
    }
}