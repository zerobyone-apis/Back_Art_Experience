package model;

import org.springframework.hateoas.core.Relation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "barber")
@Relation(value = "barber", collectionRelation = "barber")
public class Client implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "barber_sequence")
	@SequenceGenerator(name = "barber_sequence", initialValue = 1)

	//User information
	@Column(name = "id")
	private Long id;
	@Column(name = "user_id")
	private Long user_id;
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "email")
	private String email;
	@Column(name = "cel") // Debe ser un string debido a que puede ocurrir un error en la DB por el 0 si lo tratamos como INTEGER. -> PDT: Se puede utilizar un Converter con una anotacion de Spring.
	private String cel;
	@Column(name = "start_date")
	private Instant startDate;
	@Column(name = "end_date")
	private Instant endDate;

	// Client Information
	@Column(name = "name")
	private String name;
	@Column(name = "local_visited")
	private String localVisited;

	@Column(name = "total_times_attended") // Horario del barbero por si se desea realizar alguna tarea con esto.
	private String timeWorked;
	@Column(name = "count_times_attended") // Horario del barbero por si se desea realizar alguna tarea con esto.
	private String countAttended;
	@Column(name = "favorite_barber") // -> se guarda el barbero favorito en base a la cantidad de reservas con el mismo,
	private String timesCutByBarber; // -> 	private String timesCutByBarber;

	// Analytics information
	@Column(name = "total_cash_invested")
	private Long totalCashInvested;
	@Column(name = "referenced_users_id")
	private Long referencedUsersId;  // -> Lista de usuarios que ha referenciado, lo que le puede llegar a dar alguna bonificacion por traer gente a la barberia
	@Column(name = "amount_of_comments")
	private Long amountOfComments;
	@Column(name = "total_likes")
	private Long totalCountlikes;
	@Column(name = "amount_of_shareds")
	private Long amountOfShares;
	@Column(name = "list_of_stars_donned")
	private Double prestige;
	@Column(name = "amount_reserves_success")
	private Long amountReservesSuccess;
	@Column(name = "amount_reserves_canceled")
	private Long amountReservesCanceled;
	@Column(name = "amount_reserves")
	private Long amountReserves;

	//Client Status
	@Column(name = "is_active") // Borrado logico.
	private Boolean isActive;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCel() {
		return cel;
	}

	public void setCel(String cel) {
		this.cel = cel;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocalVisited() {
		return localVisited;
	}

	public void setLocalVisited(String localVisited) {
		this.localVisited = localVisited;
	}

	public String getTimeWorked() {
		return timeWorked;
	}

	public void setTimeWorked(String timeWorked) {
		this.timeWorked = timeWorked;
	}

	public String getCountAttended() {
		return countAttended;
	}

	public void setCountAttended(String countAttended) {
		this.countAttended = countAttended;
	}

	public String getTimesCutByBarber() {
		return timesCutByBarber;
	}

	public void setTimesCutByBarber(String timesCutByBarber) {
		this.timesCutByBarber = timesCutByBarber;
	}

	public Long getTotalCashInvested() {
		return totalCashInvested;
	}

	public void setTotalCashInvested(Long totalCashInvested) {
		this.totalCashInvested = totalCashInvested;
	}

	public Long getReferencedUsersId() {
		return referencedUsersId;
	}

	public void setReferencedUsersId(Long referencedUsersId) {
		this.referencedUsersId = referencedUsersId;
	}

	public Long getAmountOfComments() {
		return amountOfComments;
	}

	public void setAmountOfComments(Long amountOfComments) {
		this.amountOfComments = amountOfComments;
	}

	public Long getTotalCountlikes() {
		return totalCountlikes;
	}

	public void setTotalCountlikes(Long totalCountlikes) {
		this.totalCountlikes = totalCountlikes;
	}

	public Long getAmountOfShares() {
		return amountOfShares;
	}

	public void setAmountOfShares(Long amountOfShares) {
		this.amountOfShares = amountOfShares;
	}

	public Double getPrestige() {
		return prestige;
	}

	public void setPrestige(Double prestige) {
		this.prestige = prestige;
	}

	public Long getAmountReservesSuccess() {
		return amountReservesSuccess;
	}

	public void setAmountReservesSuccess(Long amountReservesSuccess) {
		this.amountReservesSuccess = amountReservesSuccess;
	}

	public Long getAmountReservesCanceled() {
		return amountReservesCanceled;
	}

	public void setAmountReservesCanceled(Long amountReservesCanceled) {
		this.amountReservesCanceled = amountReservesCanceled;
	}

	public Long getAmountReserves() {
		return amountReserves;
	}

	public void setAmountReserves(Long amountReserves) {
		this.amountReserves = amountReserves;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean active) {
		isActive = active;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Client client = (Client) o;
		return id.equals(client.id) &&
				Objects.equals(user_id, client.user_id) &&
				Objects.equals(username, client.username) &&
				Objects.equals(password, client.password) &&
				Objects.equals(email, client.email) &&
				Objects.equals(cel, client.cel) &&
				Objects.equals(startDate, client.startDate) &&
				Objects.equals(endDate, client.endDate) &&
				Objects.equals(name, client.name) &&
				Objects.equals(localVisited, client.localVisited) &&
				Objects.equals(timeWorked, client.timeWorked) &&
				Objects.equals(countAttended, client.countAttended) &&
				Objects.equals(timesCutByBarber, client.timesCutByBarber) &&
				Objects.equals(totalCashInvested, client.totalCashInvested) &&
				Objects.equals(referencedUsersId, client.referencedUsersId) &&
				Objects.equals(amountOfComments, client.amountOfComments) &&
				Objects.equals(totalCountlikes, client.totalCountlikes) &&
				Objects.equals(amountOfShares, client.amountOfShares) &&
				Objects.equals(prestige, client.prestige) &&
				Objects.equals(amountReservesSuccess, client.amountReservesSuccess) &&
				Objects.equals(amountReservesCanceled, client.amountReservesCanceled) &&
				Objects.equals(amountReserves, client.amountReserves) &&
				isActive.equals(client.isActive);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, user_id, username, password, email, cel, startDate, endDate, name, localVisited, timeWorked, countAttended, timesCutByBarber, totalCashInvested, referencedUsersId, amountOfComments, totalCountlikes, amountOfShares, prestige, amountReservesSuccess, amountReservesCanceled, amountReserves, isActive);
	}
}
