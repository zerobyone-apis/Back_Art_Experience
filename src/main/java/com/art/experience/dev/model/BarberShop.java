package com.art.experience.dev.model;

import org.springframework.hateoas.core.Relation;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Objects;

@Entity
@Table(name = "barber_shop")
@Relation(value = "barber_shop", collectionRelation = "barber_shop")
public class BarberShop implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "barber_shop_sequence")
    @SequenceGenerator(name = "barber_shop_sequence", allocationSize = 1)
   // Shop Information
    @Column(name = "local_id")
    private Long shopId;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "cel")
    private Integer cel;

    @Column(name = "barber_id")
    private HashMap<String , Long> barberIds;
    @Column(name = "hairdresser_id")
    private HashMap<String , Long> hairdresserIds;
    @Column(name = "open_time")
    private Instant openTime;
    @Column(name = "addresses")
    private String directions;
    @Column(name = "start_date")
    private Instant startDate;
    @Column(name = "end_date")
    private Instant endDate;
    @Column(name = "rate_of_barber_shop")
    private String rateOfBarberShop;

    @Column(name = "url_logo")
    private String urlLogo;
    @Column(name = "url_banner")
    private String url_banner;
    @Column(name = "url_folder_images")
    private String url_folder_images;

    public HashMap<String,Long> getHairdresserIds() {
        return hairdresserIds;
    }

    public void setHairdresserIds(HashMap<String,Long> hairdresserIds) {
        this.hairdresserIds = hairdresserIds;
    }

    public Instant getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Instant openTime) {
        this.openTime = openTime;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getUrl_banner() {
        return url_banner;
    }

    public void setUrl_banner(String url_banner) {
        this.url_banner = url_banner;
    }

    public String getUrl_folder_images() {
        return url_folder_images;
    }

    public void setUrl_folder_images(String url_folder_images) {
        this.url_folder_images = url_folder_images;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCel() {
        return cel;
    }

    public void setCel(Integer cel) {
        this.cel = cel;
    }

    public HashMap<String,Long> getBarberIds() {
        return barberIds;
    }

    public void setBarberIds(HashMap<String,Long> barberIds) {
        this.barberIds = barberIds;
    }


    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getRateOfBarberShop() {
        return rateOfBarberShop;
    }

    public void setRateOfBarberShop(String rateOfBarberShop) {
        this.rateOfBarberShop = rateOfBarberShop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BarberShop that = (BarberShop) o;
        return shopId.equals(that.shopId) &&
                name.equals(that.name) &&
                email.equals(that.email) &&
                cel.equals(that.cel) &&
                Objects.equals(barberIds, that.barberIds) &&
                Objects.equals(hairdresserIds, that.hairdresserIds) &&
                openTime.equals(that.openTime) &&
                directions.equals(that.directions) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(rateOfBarberShop, that.rateOfBarberShop) &&
                Objects.equals(urlLogo, that.urlLogo) &&
                Objects.equals(url_banner, that.url_banner) &&
                Objects.equals(url_folder_images, that.url_folder_images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shopId, name, email, cel, barberIds, hairdresserIds, openTime, directions, startDate, endDate, rateOfBarberShop, urlLogo, url_banner, url_folder_images);
    }
}