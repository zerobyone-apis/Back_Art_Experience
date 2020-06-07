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
public class Barber implements Serializable {

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
	@Column(name = "start_date")
	private Instant startDate;
	@Column(name = "end_date")
	private Instant endDate;

	// Barber Information
	@Column(name = "name")
	private String name;
	@Column(name = "local_id")
	private Long localId;
	@Column(name = "local_name")
	private String localName;
	@Column(name = "email")
	private String email;
	@Column(name = "cel") // Debe ser un string debido a que puede ocurrir un error en la DB por el 0 si lo tratamos como INTEGER. -> PDT: Se puede utilizar un Converter con una anotacion de Spring.
	private String cel;
	@Column(name = "times_worked") // Horario del barbero por si se desea realizar alguna tarea con esto.
	private String timeWorked;
	@Column(name = "times_of_barber") // -> Tiempos de Corte duración.
	private String timeOfBarbers;

	// Analytics information
	@Column(name = "amount_of_cuts")
	private Long amount_of_cuts;
	@Column(name = "amount_of_clients")
	private Long amountOfClients;
	@Column(name = "amount_of_comments")
	private Long amountOfComments;
	@Column(name = "amount_of_likes_on_comments")
	private Long amountOflikesOnComments;
	@Column(name = "amount_of_shareds")
	private Long amountOfShares;
	@Column(name = "prestige")
	private Long prestige;
	@Column(name = "amount_daily_reserves")
	private Long amountDailyReserves;
	@Column(name = "status") // Borrado logico.
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

	public Long getLocalId() {
		return localId;
	}

	public void setLocalId(Long localId) {
		this.localId = localId;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
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

	public String getTimeWorked() {
		return timeWorked;
	}

	public void setTimeWorked(String timeWorked) {
		this.timeWorked = timeWorked;
	}

	public String getTimeOfBarbers() {
		return timeOfBarbers;
	}

	public void setTimeOfBarbers(String timeOfBarbers) {
		this.timeOfBarbers = timeOfBarbers;
	}

	public Long getAmount_of_cuts() {
		return amount_of_cuts;
	}

	public void setAmount_of_cuts(Long amount_of_cuts) {
		this.amount_of_cuts = amount_of_cuts;
	}

	public Long getAmountOfClients() {
		return amountOfClients;
	}

	public void setAmountOfClients(Long amountOfClients) {
		this.amountOfClients = amountOfClients;
	}

	public Long getAmountOfComments() {
		return amountOfComments;
	}

	public void setAmountOfComments(Long amountOfComments) {
		this.amountOfComments = amountOfComments;
	}

	public Long getAmountOflikesOnComments() {
		return amountOflikesOnComments;
	}

	public void setAmountOflikesOnComments(Long amountOflikesOnComments) {
		this.amountOflikesOnComments = amountOflikesOnComments;
	}

	public Long getAmountOfShares() {
		return amountOfShares;
	}

	public void setAmountOfShares(Long amountOfShares) {
		this.amountOfShares = amountOfShares;
	}

	public Long getPrestige() {
		return prestige;
	}

	public void setPrestige(Long prestige) {
		this.prestige = prestige;
	}

	public Long getAmountDailyReserves() {
		return amountDailyReserves;
	}

	public void setAmountDailyReserves(Long amountDailyReserves) {
		this.amountDailyReserves = amountDailyReserves;
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
		Barber barber = (Barber) o;
		return Objects.equals(id, barber.id) &&
				Objects.equals(user_id, barber.user_id) &&
				Objects.equals(username, barber.username) &&
				Objects.equals(password, barber.password) &&
				Objects.equals(startDate, barber.startDate) &&
				Objects.equals(endDate, barber.endDate) &&
				Objects.equals(name, barber.name) &&
				Objects.equals(localId, barber.localId) &&
				Objects.equals(localName, barber.localName) &&
				Objects.equals(email, barber.email) &&
				Objects.equals(cel, barber.cel) &&
				Objects.equals(timeWorked, barber.timeWorked) &&
				Objects.equals(timeOfBarbers, barber.timeOfBarbers) &&
				Objects.equals(amount_of_cuts, barber.amount_of_cuts) &&
				Objects.equals(amountOfClients, barber.amountOfClients) &&
				Objects.equals(amountOfComments, barber.amountOfComments) &&
				Objects.equals(amountOflikesOnComments, barber.amountOflikesOnComments) &&
				Objects.equals(amountOfShares, barber.amountOfShares) &&
				Objects.equals(prestige, barber.prestige) &&
				Objects.equals(amountDailyReserves, barber.amountDailyReserves) &&
				Objects.equals(isActive, barber.isActive);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, user_id, username, password, startDate, endDate, name, localId, localName, email, cel, timeWorked, timeOfBarbers, amount_of_cuts, amountOfClients, amountOfComments, amountOflikesOnComments, amountOfShares, prestige, amountDailyReserves, isActive);
	}
}

