package jp.co.workscm.ec.AuthenticationService.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author li_zh
 *
 */
@Entity
@Table(name = "USER")
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
	@NamedQuery(name = User.FIND_BY_USERNAME_AND_PASSWORD, query = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password"),
	@NamedQuery(name = User.FIND_ALL, query = "select u FROM User u ORDER BY u.id ASC")
})
public class User {

	public static final String FIND_BY_USERNAME_AND_PASSWORD = "User.findByUsernameAndPassword";
	public static final String FIND_ALL = "User.findAll";
	@Getter @Id @Column(name = "ID")
	private long id;
	@Setter @Getter @Column(name = "USER_NAME")
	private String username;
	@Setter @Getter @Column(name = "PASSWORD")
	private String password;
	@Setter @Getter @Column(name = "FIRST_NAME")
	private String firstName;
	@Setter @Getter @Column(name = "LAST_NAME")
	private String lastName;
}
