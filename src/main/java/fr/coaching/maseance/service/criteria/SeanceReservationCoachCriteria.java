package fr.coaching.maseance.service.criteria;

import fr.coaching.maseance.domain.enumeration.CanalSeance;
import fr.coaching.maseance.domain.enumeration.StatutSeance;
import fr.coaching.maseance.domain.enumeration.TypeSeance;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.coaching.maseance.domain.SeanceReservationCoach} entity. This class is used
 * in {@link fr.coaching.maseance.web.rest.SeanceReservationCoachResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /seance-reservation-coaches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SeanceReservationCoachCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CanalSeance
     */
    public static class CanalSeanceFilter extends Filter<CanalSeance> {

        public CanalSeanceFilter() {}

        public CanalSeanceFilter(CanalSeanceFilter filter) {
            super(filter);
        }

        @Override
        public CanalSeanceFilter copy() {
            return new CanalSeanceFilter(this);
        }
    }

    /**
     * Class for filtering TypeSeance
     */
    public static class TypeSeanceFilter extends Filter<TypeSeance> {

        public TypeSeanceFilter() {}

        public TypeSeanceFilter(TypeSeanceFilter filter) {
            super(filter);
        }

        @Override
        public TypeSeanceFilter copy() {
            return new TypeSeanceFilter(this);
        }
    }

    /**
     * Class for filtering StatutSeance
     */
    public static class StatutSeanceFilter extends Filter<StatutSeance> {

        public StatutSeanceFilter() {}

        public StatutSeanceFilter(StatutSeanceFilter filter) {
            super(filter);
        }

        @Override
        public StatutSeanceFilter copy() {
            return new StatutSeanceFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter heureDebutCreneauReserve;

    private InstantFilter heureFinCreneauReserve;

    private CanalSeanceFilter canalSeance;

    private TypeSeanceFilter typeSeance;

    private StatutSeanceFilter statutRealisation;

    private LongFilter factureId;

    private LongFilter coachExpertId;

    private LongFilter clientId;

    private LongFilter offreId;

    private Boolean distinct;

    public SeanceReservationCoachCriteria() {}

    public SeanceReservationCoachCriteria(SeanceReservationCoachCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.heureDebutCreneauReserve = other.optionalHeureDebutCreneauReserve().map(InstantFilter::copy).orElse(null);
        this.heureFinCreneauReserve = other.optionalHeureFinCreneauReserve().map(InstantFilter::copy).orElse(null);
        this.canalSeance = other.optionalCanalSeance().map(CanalSeanceFilter::copy).orElse(null);
        this.typeSeance = other.optionalTypeSeance().map(TypeSeanceFilter::copy).orElse(null);
        this.statutRealisation = other.optionalStatutRealisation().map(StatutSeanceFilter::copy).orElse(null);
        this.factureId = other.optionalFactureId().map(LongFilter::copy).orElse(null);
        this.coachExpertId = other.optionalCoachExpertId().map(LongFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(LongFilter::copy).orElse(null);
        this.offreId = other.optionalOffreId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SeanceReservationCoachCriteria copy() {
        return new SeanceReservationCoachCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getHeureDebutCreneauReserve() {
        return heureDebutCreneauReserve;
    }

    public Optional<InstantFilter> optionalHeureDebutCreneauReserve() {
        return Optional.ofNullable(heureDebutCreneauReserve);
    }

    public InstantFilter heureDebutCreneauReserve() {
        if (heureDebutCreneauReserve == null) {
            setHeureDebutCreneauReserve(new InstantFilter());
        }
        return heureDebutCreneauReserve;
    }

    public void setHeureDebutCreneauReserve(InstantFilter heureDebutCreneauReserve) {
        this.heureDebutCreneauReserve = heureDebutCreneauReserve;
    }

    public InstantFilter getHeureFinCreneauReserve() {
        return heureFinCreneauReserve;
    }

    public Optional<InstantFilter> optionalHeureFinCreneauReserve() {
        return Optional.ofNullable(heureFinCreneauReserve);
    }

    public InstantFilter heureFinCreneauReserve() {
        if (heureFinCreneauReserve == null) {
            setHeureFinCreneauReserve(new InstantFilter());
        }
        return heureFinCreneauReserve;
    }

    public void setHeureFinCreneauReserve(InstantFilter heureFinCreneauReserve) {
        this.heureFinCreneauReserve = heureFinCreneauReserve;
    }

    public CanalSeanceFilter getCanalSeance() {
        return canalSeance;
    }

    public Optional<CanalSeanceFilter> optionalCanalSeance() {
        return Optional.ofNullable(canalSeance);
    }

    public CanalSeanceFilter canalSeance() {
        if (canalSeance == null) {
            setCanalSeance(new CanalSeanceFilter());
        }
        return canalSeance;
    }

    public void setCanalSeance(CanalSeanceFilter canalSeance) {
        this.canalSeance = canalSeance;
    }

    public TypeSeanceFilter getTypeSeance() {
        return typeSeance;
    }

    public Optional<TypeSeanceFilter> optionalTypeSeance() {
        return Optional.ofNullable(typeSeance);
    }

    public TypeSeanceFilter typeSeance() {
        if (typeSeance == null) {
            setTypeSeance(new TypeSeanceFilter());
        }
        return typeSeance;
    }

    public void setTypeSeance(TypeSeanceFilter typeSeance) {
        this.typeSeance = typeSeance;
    }

    public StatutSeanceFilter getStatutRealisation() {
        return statutRealisation;
    }

    public Optional<StatutSeanceFilter> optionalStatutRealisation() {
        return Optional.ofNullable(statutRealisation);
    }

    public StatutSeanceFilter statutRealisation() {
        if (statutRealisation == null) {
            setStatutRealisation(new StatutSeanceFilter());
        }
        return statutRealisation;
    }

    public void setStatutRealisation(StatutSeanceFilter statutRealisation) {
        this.statutRealisation = statutRealisation;
    }

    public LongFilter getFactureId() {
        return factureId;
    }

    public Optional<LongFilter> optionalFactureId() {
        return Optional.ofNullable(factureId);
    }

    public LongFilter factureId() {
        if (factureId == null) {
            setFactureId(new LongFilter());
        }
        return factureId;
    }

    public void setFactureId(LongFilter factureId) {
        this.factureId = factureId;
    }

    public LongFilter getCoachExpertId() {
        return coachExpertId;
    }

    public Optional<LongFilter> optionalCoachExpertId() {
        return Optional.ofNullable(coachExpertId);
    }

    public LongFilter coachExpertId() {
        if (coachExpertId == null) {
            setCoachExpertId(new LongFilter());
        }
        return coachExpertId;
    }

    public void setCoachExpertId(LongFilter coachExpertId) {
        this.coachExpertId = coachExpertId;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public Optional<LongFilter> optionalClientId() {
        return Optional.ofNullable(clientId);
    }

    public LongFilter clientId() {
        if (clientId == null) {
            setClientId(new LongFilter());
        }
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public LongFilter getOffreId() {
        return offreId;
    }

    public Optional<LongFilter> optionalOffreId() {
        return Optional.ofNullable(offreId);
    }

    public LongFilter offreId() {
        if (offreId == null) {
            setOffreId(new LongFilter());
        }
        return offreId;
    }

    public void setOffreId(LongFilter offreId) {
        this.offreId = offreId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SeanceReservationCoachCriteria that = (SeanceReservationCoachCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(heureDebutCreneauReserve, that.heureDebutCreneauReserve) &&
            Objects.equals(heureFinCreneauReserve, that.heureFinCreneauReserve) &&
            Objects.equals(canalSeance, that.canalSeance) &&
            Objects.equals(typeSeance, that.typeSeance) &&
            Objects.equals(statutRealisation, that.statutRealisation) &&
            Objects.equals(factureId, that.factureId) &&
            Objects.equals(coachExpertId, that.coachExpertId) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(offreId, that.offreId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            heureDebutCreneauReserve,
            heureFinCreneauReserve,
            canalSeance,
            typeSeance,
            statutRealisation,
            factureId,
            coachExpertId,
            clientId,
            offreId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SeanceReservationCoachCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalHeureDebutCreneauReserve().map(f -> "heureDebutCreneauReserve=" + f + ", ").orElse("") +
            optionalHeureFinCreneauReserve().map(f -> "heureFinCreneauReserve=" + f + ", ").orElse("") +
            optionalCanalSeance().map(f -> "canalSeance=" + f + ", ").orElse("") +
            optionalTypeSeance().map(f -> "typeSeance=" + f + ", ").orElse("") +
            optionalStatutRealisation().map(f -> "statutRealisation=" + f + ", ").orElse("") +
            optionalFactureId().map(f -> "factureId=" + f + ", ").orElse("") +
            optionalCoachExpertId().map(f -> "coachExpertId=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalOffreId().map(f -> "offreId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
