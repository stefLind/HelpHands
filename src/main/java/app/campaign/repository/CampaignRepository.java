package app.campaign.repository;

import app.campaign.model.Campaign;
import app.campaign.model.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, UUID> {

    List<Campaign> findAllByStatusOrderByCreatedOnDesc(CampaignStatus status);

    // Custom query to filter the data
    // lanuage=MySQL
    @Query(value = "select c.* from campaign c, user u where c.creator_id = u.id and (%:status% is null or c.status = %:status%) and (%:type% is null or c.type = %:type%) and (%:creator% is null or u.first_name like %:creator% or u.company_name like %:creator%) and (%:location% is null or c.location like %:location%) order by c.created_on desc", nativeQuery = true)
    List<Campaign> findByFilterCriteria(@Param("status") String status, @Param("type") String type, @Param("creator") String creator, @Param("location") String location);

}
