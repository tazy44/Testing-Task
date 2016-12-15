import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.Times;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class Utest {
    @Mock
    Connection conn;
    @Mock
    PreparedStatement psmt;
    @InjectMocks
    DAOImpl newDao = new DAOImpl();

    DAOImpl realDao = new DAOImpl();

    @Test
    public void TestProductConstructor()
    {
        Product newP = new Product(345);
        Assert.assertEquals(newP.getId(), 345);
    }

    @Test
    public void TestProductTypeSetter () {
        Product newP = new Product(345);
        newP.setType("Radio Therapy");
        Assert.assertEquals(newP.getType(), "Radio Therapy");
    }

    @Test
    public void TestProductManufacturerSetter () {
        Product newP = new Product(345);
        newP.setManufacturer("Siemens");
        Assert.assertEquals(newP.getManufacturer(), "Siemens");
    }

    @Test
    public void TestProductExpirySetter () {
        Product newP = new Product(345);
        newP.setExpiryDate("1/1/2020");
        Assert.assertEquals(newP.getExpiryDate(), "1/1/2020");
    }

    @Test
    public void TestProductProductionSetter () {
        Product newP = new Product(345);
        newP.setProductionDate("1/1/2012");
        Assert.assertEquals(newP.getProductionDate(), "1/1/2012");
    }

    @Test
    public void TestUpdateHappyCaseAsserts () throws SQLException, DAOException {
        when(conn.prepareStatement(anyString())).thenReturn(psmt);

        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> intCaptor = ArgumentCaptor.forClass(Integer.class);

        Product newP = new Product(345);
        newP.setProductionDate("1/1/2012");
        newP.setExpiryDate("1/1/2020");
        newP.setManufacturer("Siemens");
        newP.setType("Radio Therapy");
        newDao.updateProduct(newP);

        verify(psmt, times(4)).setString(anyInt(), stringCaptor.capture());
        verify(psmt, times(1)).setInt(anyInt(), intCaptor.capture());
        Assert.assertTrue(intCaptor.getAllValues().get(0).equals(345));
        Assert.assertTrue(stringCaptor.getAllValues().get(0).equals("Radio Therapy"));
        Assert.assertTrue(stringCaptor.getAllValues().get(1).equals("Siemens"));
        Assert.assertTrue(stringCaptor.getAllValues().get(2).equals("1/1/2012"));
        Assert.assertTrue(stringCaptor.getAllValues().get(3).equals("1/1/2020"));
    }

    @Test (expected = DAOException.class)
    public void ExceptionCase() throws SQLException, DAOException{
        when(conn.prepareStatement(anyString())).thenReturn(psmt);
        when(psmt.executeUpdate()).thenThrow(new SQLException());

        Product newP = new Product(345);
        newDao.updateProduct(newP);
    }

    @Test
    public void TestDeleteHappyCaseIntegration () throws SQLException, DAOException {

        Product newP = new Product(345);
        newP.setProductionDate("1/1/2012");
        newP.setExpiryDate("1/1/2020");
        newP.setManufacturer("Siemens");
        newP.setType("Radio Therapy");
        realDao.insertProduct(newP);

        Product retrievedProduct = realDao.getProduct(345);
        Assert.assertNotNull(retrievedProduct);

        realDao.deleteProduct(345);
        Product retrievedProductAfterDeletion = realDao.getProduct(345);
        Assert.assertNull(retrievedProductAfterDeletion);
    }
}
