package de.felix_klauke.doctrin.server;

import de.felix_klauke.doctrin.commons.message.DoctrinMessage;
import de.felix_klauke.doctrin.commons.message.DoctrinMessageContext;
import de.felix_klauke.doctrin.commons.message.DoctrinMessageWrapper;
import de.felix_klauke.doctrin.core.DoctrinCoreApplication;
import de.felix_klauke.doctrin.server.connection.DoctrinServerConnection;
import io.reactivex.subjects.BehaviorSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test for {@link DoctrinServerApplicationImpl}.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
@RunWith(MockitoJUnitRunner.class)
public class DoctrinServerApplicationImplTest {

    @Mock
    DoctrinServerConnection doctrinServerConnection;
    @Mock
    DoctrinCoreApplication doctrinCoreApplication;

    @Mock
    DoctrinMessage doctrinMessage;
    @Mock
    DoctrinMessageContext doctrinMessageContext;

    private DoctrinServerApplicationImpl doctrinServerApplication;

    @Before
    public void setUp() {
        doctrinServerApplication = new DoctrinServerApplicationImpl(doctrinCoreApplication);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void handleNewConnection() {
        // Given
        BehaviorSubject<DoctrinMessageWrapper> doctrinMessageWrapperBehaviorSubject
                = BehaviorSubject.create();
        DoctrinMessageWrapper doctrinMessageWrapper = new DoctrinMessageWrapper(doctrinMessage, doctrinMessageContext);

        // When
        Mockito.when(doctrinServerConnection.getMessages()).thenReturn(doctrinMessageWrapperBehaviorSubject);
        
        doctrinServerApplication.handleNewConnection(doctrinServerConnection);
        doctrinMessageWrapperBehaviorSubject.onNext(doctrinMessageWrapper);

        // Then
        Mockito.verify(doctrinCoreApplication).handleMessage(doctrinMessageWrapper.getMessageContext(), doctrinMessageWrapper.getMessage());
    }

    @Test
    public void initialize() {
        doctrinServerApplication.initialize();
    }
}