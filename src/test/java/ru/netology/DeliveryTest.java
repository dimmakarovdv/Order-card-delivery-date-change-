package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 4;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        int daysToAddForSecondMeeting = 7;
        String secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $("[data-test-id=city] input").setValue(validUser.getCity());

        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);

        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();

        // Проверка успешного планирования
        $("[data-test-id=success-notification]")
                .shouldBe(visible, Duration.ofSeconds(20))
                .shouldHave(text("Успешно!"));
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate));

        // Перепланирование встречи
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(".button").click();

        // Подтверждение перепланирования
        $("[data-test-id=replan-notification]")
                .shouldBe(visible, Duration.ofSeconds(20))
                .shouldHave(text("Необходимо подтверждение"));
        $("[data-test-id=replan-notification] .button").click();

        // Проверка успешного перепланирования
        $("[data-test-id=success-notification]")
                .shouldBe(visible, Duration.ofSeconds(20))
                .shouldHave(text("Успешно!"));
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate));
    }
}