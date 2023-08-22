package tr.com.cemalaydin.dashboard.entities;

import tr.com.cemalaydin.dashboard.base.BaseEntity;
import tr.com.cemalaydin.dashboard.enums.HatimDurum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.Date;

@Entity
@Where(clause = "status >=0")
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Hatim extends BaseEntity {


    private String aciklama;

    private HatimDurum durum;
    @NotNull
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User olusturan;

    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date bitisTarihi;

    private Boolean herkeseAcik = false;


    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public HatimDurum getDurum() {
        return durum;
    }

    public void setDurum(HatimDurum durum) {
        this.durum = durum;
    }

    public User getOlusturan() {
        return olusturan;
    }

    public void setOlusturan(User olusturan) {
        this.olusturan = olusturan;
    }

    public Date getBitisTarihi() {
        return bitisTarihi;
    }

    public void setBitisTarihi(Date bitisTarihi) {
        this.bitisTarihi = bitisTarihi;
    }

    public Boolean getHerkeseAcik() {
        return herkeseAcik;
    }

    public void setHerkeseAcik(Boolean herkeseAcik) {
        this.herkeseAcik = herkeseAcik;
    }
}
