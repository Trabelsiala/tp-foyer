package tn.esprit.tpfoyer.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.tpfoyer.entity.Bloc;

import java.util.List;

@Repository
public interface BlocRepository extends JpaRepository<Bloc, Long> {

 // Méthode pour trouver des blocs par plage de capacité avec tri
 List<Bloc> findAllByCapaciteBlocBetween(long minCapacity, long maxCapacity, Sort sort);

 // Récupérer les blocs sans foyer associé
 List<Bloc> findAllByFoyerIsNull();

 // Récupérer tous les blocs ayant un nom et une capacité donnés
 List<Bloc> findAllByNomBlocAndCapaciteBloc(String nom, long capacite);
}