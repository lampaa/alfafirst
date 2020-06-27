/*
 * Сервис проверки статуса банкоматов
 * Сервис, возвращающий информацию о банкоматах Альфа-Банка
 *
 * The version of the OpenAPI document: 1.0.0
 * Contact: apisupport@alfabank.ru
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.lampa.alfabattle.first.dto;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * ATMAccess
 */
@JsonPropertyOrder({
        ATMAccess.JSON_PROPERTY_MODE,
        ATMAccess.JSON_PROPERTY_SCHEDULE
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2020-06-27T10:26:33.214272+03:00[Europe/Moscow]")
public class ATMAccess {
    /**
     * Доступность АТМ для клиента, принимает следующие значения:  alltime &#x3D; круглосуточно  schedule &#x3D; по расписанию работы организации. В этом случае расписание указывается в поле schedule.
     */
    public enum ModeEnum {
        ALLTIME("alltime"),

        SCHEDULE("schedule"),

        NOINFO("noinfo");

        private String value;

        ModeEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static ModeEnum fromValue(String value) {
            for (ModeEnum b : ModeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public static final String JSON_PROPERTY_MODE = "mode";
    private ModeEnum mode;

    public static final String JSON_PROPERTY_SCHEDULE = "schedule";
    private String schedule;


    public ATMAccess mode(ModeEnum mode) {

        this.mode = mode;
        return this;
    }

    /**
     * Доступность АТМ для клиента, принимает следующие значения:  alltime &#x3D; круглосуточно  schedule &#x3D; по расписанию работы организации. В этом случае расписание указывается в поле schedule.
     *
     * @return mode
     **/
    @ApiModelProperty(value = "Доступность АТМ для клиента, принимает следующие значения:  alltime = круглосуточно  schedule = по расписанию работы организации. В этом случае расписание указывается в поле schedule.")
    @JsonProperty(JSON_PROPERTY_MODE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

    public ModeEnum getMode() {
        return mode;
    }


    public void setMode(ModeEnum mode) {
        this.mode = mode;
    }


    public ATMAccess schedule(String schedule) {

        this.schedule = schedule;
        return this;
    }

    /**
     * Указывается, если mode&#x3D;schedule. Массив [DD:hhmm–hhmm,DD:hhmm–hhmm,DD:hhmm–hhmm,…] DD &#x3D; [MO, TU, WE, TH, FR, SA, SU] hh &#x3D; 00..24 mm &#x3D; 00..59 Время указывается местное (места установки АТМ) Если в течение дня есть перерыв, то указывается каждый диапазон времени в течение одного дня. Например, MO:0800-1300,MO:1400-2100,TU:0800-1300,TU:1400-2100
     *
     * @return schedule
     **/
    @ApiModelProperty(value = "Указывается, если mode=schedule. Массив [DD:hhmm–hhmm,DD:hhmm–hhmm,DD:hhmm–hhmm,…] DD = [MO, TU, WE, TH, FR, SA, SU] hh = 00..24 mm = 00..59 Время указывается местное (места установки АТМ) Если в течение дня есть перерыв, то указывается каждый диапазон времени в течение одного дня. Например, MO:0800-1300,MO:1400-2100,TU:0800-1300,TU:1400-2100")
    @JsonProperty(JSON_PROPERTY_SCHEDULE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

    public String getSchedule() {
        return schedule;
    }


    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ATMAccess atMAccess = (ATMAccess) o;
        return Objects.equals(this.mode, atMAccess.mode) &&
                Objects.equals(this.schedule, atMAccess.schedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, schedule);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ATMAccess {\n");
        sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
        sb.append("    schedule: ").append(toIndentedString(schedule)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}

