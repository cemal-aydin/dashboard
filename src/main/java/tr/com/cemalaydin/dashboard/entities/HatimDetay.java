package tr.com.cemalaydin.dashboard.entities;

import tr.com.cemalaydin.dashboard.base.BaseEntity;
import tr.com.cemalaydin.dashboard.enums.CuzDurum;
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
public class HatimDetay extends BaseEntity implements Cloneable {

    private int cuz;

    private CuzDurum durum;
    @NotNull
    @JoinColumn(name = "hatim_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Hatim hatim;


    @JoinColumn(name = "okuyan_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User okuyan;

    @Column()
    @Temporal(TemporalType.DATE)
    private Date atamaZamani;

    @Column()
    @Temporal(TemporalType.DATE)
    private Date okumaZamani;

    @Column()
    private String  okuyanAdi;

    @JoinColumn(name = "okundu_yapan_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User okunduYapan;

    @Override
    public HatimDetay clone() throws CloneNotSupportedException {
        return (HatimDetay)super.clone();
    }

    public int getCuz() {
        return cuz;
    }

    public void setCuz(int cuz) {
        this.cuz = cuz;
    }

    public CuzDurum getDurum() {
        return durum;
    }

    public void setDurum(CuzDurum durum) {
        this.durum = durum;
    }

    public Hatim getHatim() {
        return hatim;
    }

    public void setHatim(Hatim hatim) {
        this.hatim = hatim;
    }

    public User getOkuyan() {
        return okuyan;
    }

    public void setOkuyan(User okuyan) {
        this.okuyan = okuyan;
    }

    public Date getAtamaZamani() {
        return atamaZamani;
    }

    public void setAtamaZamani(Date atamaZamani) {
        this.atamaZamani = atamaZamani;
    }

    public Date getOkumaZamani() {
        return okumaZamani;
    }

    public void setOkumaZamani(Date okumaZamani) {
        this.okumaZamani = okumaZamani;
    }

    public User getOkunduYapan() {
        return okunduYapan;
    }

    public void setOkunduYapan(User okunduYapan) {
        this.okunduYapan = okunduYapan;
    }

    public String getOkuyanAdi() {
        return okuyanAdi;
    }

    public void setOkuyanAdi(String okuyanAdi) {
        this.okuyanAdi = okuyanAdi;
    }
}
