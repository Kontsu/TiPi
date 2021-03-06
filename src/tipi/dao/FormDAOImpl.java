package tipi.dao;

/**
 * @author Lauri Soivi, Joona Viertola, Samuel Kontiomaa
 * @version 1.0
 * @since 18.12.2013
 * DAO saves order form to database
 */

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import tipi.bean.OrderForm;


@Repository
public class FormDAOImpl implements FormDAO {

	@Inject
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Method parses date and time to sql form (dd.MM.yyyy to yyyy-MM-dd)
	 * Method adds order to database
	 */
	@Override
	public void saveOrderFormDAO(OrderForm orderForm, int userID, int userCompanyID) {

		SimpleDateFormat oldFormat = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");

		String collectionDate = null;
		String destinationDate = null;
		String nextDestinationCollectionDate = null;
		String nextDestinationDate = null;

		try {
			collectionDate = newFormat.format(oldFormat.parse(orderForm
					.getCollectionDate()));
			destinationDate = newFormat.format(oldFormat.parse(orderForm
					.getDestinationDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(orderForm.isHasNewDestination()) {
		try {
			nextDestinationCollectionDate = newFormat.format(oldFormat
					.parse(orderForm.getNextDestinationCollectionDate()));
			nextDestinationDate = newFormat.format(oldFormat.parse(orderForm
					.getNextDestinationDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String sql = "INSERT INTO orders ( carBrand, carModel, carRegister, carColor,"
				+ " collectionDate, collectionTime, collectionAddress, collectionPostalCode, collectionCity,"
				+ " destinationDate, destinationTime, destinationAddress, destinationPostalCode, destinationCity,"
				+ " clientFname, clientLname, clientPhoneNo, clientCompany, additionalInformation,"
				+ " companyMadeOrder, userMadeOrder, hasNewDestination,"
				+ "	nextDestinationCollectionDate, nextDestinationCollectionTime, nextDestinationDate,	nextDestinationTime,"
				+ " nextDestinationAddress, nextDestinationPostalCode, nextDestinationCity, "
				+ "nextAdditionalInformation) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

		jdbcTemplate.update(
				sql,
				new Object[] { orderForm.getCarBrand(),
						orderForm.getCarModel(), orderForm.getCarRegister(),
						orderForm.getCarColor(), collectionDate,
						orderForm.getCollectionTime(),
						orderForm.getCollectionAddress(),
						orderForm.getCollectionPostalCode(),
						orderForm.getCollectionCity(), destinationDate,
						orderForm.getDestinationTime(),
						orderForm.getDestinationAddress(),
						orderForm.getDestinationPostalCode(),
						orderForm.getDestinationCity(),
						orderForm.getClientFname(), orderForm.getClientLname(),
						orderForm.getClientPhoneNo(),
						orderForm.getClientCompany(),
						orderForm.getAdditionalInformation(), userCompanyID, userID,
						orderForm.isHasNewDestination(),
						nextDestinationCollectionDate,
						orderForm.getNextDestinationCollectionTime(),
						nextDestinationDate,
						orderForm.getNextDestinationTime(),
						orderForm.getNextDestinationAddress(),
						orderForm.getNextDestinationPostalCode(),
						orderForm.getNextDestinationCity(),
						orderForm.getNextAdditionalInformation() });
		} else {
			String sql = "INSERT INTO orders ( carBrand, carModel, carRegister, carColor,"
					+ " collectionDate, collectionTime, collectionAddress, collectionPostalCode, collectionCity,"
					+ " destinationDate, destinationTime, destinationAddress, destinationPostalCode, destinationCity,"
					+ " clientFname, clientLname, clientPhoneNo, clientCompany, additionalInformation,"
					+ " companyMadeOrder, userMadeOrder, hasNewDestination)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			jdbcTemplate.update(
					sql,
					new Object[] { orderForm.getCarBrand(),
							orderForm.getCarModel(), orderForm.getCarRegister(),
							orderForm.getCarColor(), collectionDate,
							orderForm.getCollectionTime(),
							orderForm.getCollectionAddress(),
							orderForm.getCollectionPostalCode(),
							orderForm.getCollectionCity(), destinationDate,
							orderForm.getDestinationTime(),
							orderForm.getDestinationAddress(),
							orderForm.getDestinationPostalCode(),
							orderForm.getDestinationCity(),
							orderForm.getClientFname(), orderForm.getClientLname(),
							orderForm.getClientPhoneNo(),
							orderForm.getClientCompany(),
							orderForm.getAdditionalInformation(), userCompanyID, userID,
							orderForm.isHasNewDestination()
							});
		}
	}

}