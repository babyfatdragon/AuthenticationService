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

import lombok.Getter;

/**
 * 
 * @author li_zh
 *
 */
@Entity
@Table(name = "USER")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
	@NamedQuery(name = User.FIND_BY_ML_ADDRESS_AND_PASSWORD, query = "SELECT u FROM USER u WHERE u.ml_address = :mlAddress AND u.password = :password"),
	@NamedQuery(name = User.FIND_ALL, query = "select u FROM USER u ORDER BY u.cstId ASC")
})
public class User {

	public static final String FIND_BY_ML_ADDRESS_AND_PASSWORD = "User.findByMlAddressAndPassword";
	public static final String FIND_ALL = "User.findAll";
	@Getter @Id @Column(name = "ID")
	private long id;
	@Column(name = "USER_NAME")
	private String userName;
	@Column(name = "PASSWORD")
	private String password;
	@Column(name = "FIRST_NAME")
	private String firstName;
	@Column(name = "LAST_NAME")
	private String lastName;
}
