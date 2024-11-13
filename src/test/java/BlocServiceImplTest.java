import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import tn.esprit.tpfoyer.TpFoyerApplication;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.repository.BlocRepository;
import tn.esprit.tpfoyer.service.BlocServiceImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TpFoyerApplication.class)  // Specify the main application class here
@Transactional
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class
})
@ActiveProfiles("test")
class BlocServiceImplTest {

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private BlocServiceImpl blocService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllBlocs() {
        // Arrange
        List<Bloc> blocs = Arrays.asList(
                new Bloc(1L, "BlocA", 10, new Foyer(), new HashSet<>()),
                new Bloc(2L, "BlocB", 20, new Foyer(), new HashSet<>())
        );
        when(blocRepository.findAll()).thenReturn(blocs);

        // Act
        List<Bloc> result = blocService.retrieveAllBlocs();

        // Assert
        assertEquals(2, result.size());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveBlocsSelonCapacite() {
        // Arrange
        List<Bloc> blocs = Arrays.asList(
                new Bloc(1L, "BlocA", 10, new Foyer(), new HashSet<>()),
                new Bloc(2L, "BlocB", 20, new Foyer(), new HashSet<>()),
                new Bloc(3L, "BlocC", 15, new Foyer(), new HashSet<>())
        );
        when(blocRepository.findAll()).thenReturn(blocs);

        // Act
        List<Bloc> result = blocService.retrieveBlocsSelonCapacite(15);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(bloc -> bloc.getCapaciteBloc() >= 15));
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveBloc() {
        // Arrange
        Bloc bloc = new Bloc(1L, "BlocA", 10, new Foyer(), new HashSet<>());
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        // Act
        Bloc result = blocService.retrieveBloc(1L);

        // Assert
        assertEquals(bloc, result);
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    void testAddBloc() {
        // Arrange
        Bloc bloc = new Bloc(1L, "BlocA", 10, new Foyer(), new HashSet<>());
        when(blocRepository.save(any(Bloc.class))).thenReturn(new Bloc(1L, "BlocA", 10, new Foyer(), new HashSet<>()));

        // Act
        Bloc result = blocService.addBloc(bloc);

        // Assert
        assertEquals("BlocA", result.getNomBloc());
        assertEquals(10, result.getCapaciteBloc());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    void testModifyBloc() {
        // Arrange
        Bloc bloc = new Bloc(1L, "BlocA", 20, new Foyer(), new HashSet<>());
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);

        // Act
        Bloc result = blocService.modifyBloc(bloc);

        // Assert
        assertEquals(20, result.getCapaciteBloc());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    void testRemoveBloc() {
        // Act
        blocService.removeBloc(1L);

        // Assert
        verify(blocRepository, times(1)).deleteById(1L);
    }

    @Test
    void testTrouverBlocsSansFoyer() {
        // Arrange
        List<Bloc> blocs = Arrays.asList(
                new Bloc(1L, "BlocA", 10, null, new HashSet<>()),
                new Bloc(2L, "BlocB", 20, null, new HashSet<>())
        );
        when(blocRepository.findAllByFoyerIsNull()).thenReturn(blocs);

        // Act
        List<Bloc> result = blocService.trouverBlocsSansFoyer();

        // Assert
        assertEquals(2, result.size());
        verify(blocRepository, times(1)).findAllByFoyerIsNull();
    }

    @Test
    void testTrouverBlocsParNomEtCap() {
        // Arrange
        List<Bloc> blocs = Arrays.asList(
                new Bloc(1L, "BlocA", 15, new Foyer(), new HashSet<>())
        );
        when(blocRepository.findAllByNomBlocAndCapaciteBloc("BlocA", 15)).thenReturn(blocs);

        // Act
        List<Bloc> result = blocService.trouverBlocsParNomEtCap("BlocA", 15);

        // Assert
        assertEquals(1, result.size());
        assertEquals("BlocA", result.get(0).getNomBloc());
        verify(blocRepository, times(1)).findAllByNomBlocAndCapaciteBloc("BlocA", 15);
    }
}
