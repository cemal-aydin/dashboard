package tr.com.cemalaydin.dashboard.entities;


import tr.com.cemalaydin.dashboard.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "user_relation")
@Where(clause = "status >=0")
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserRelation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relation_user_id")
    private User relationUser;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getRelationUser() {
        return relationUser;
    }

    public void setRelationUser(User relationUser) {
        this.relationUser = relationUser;
    }
}
