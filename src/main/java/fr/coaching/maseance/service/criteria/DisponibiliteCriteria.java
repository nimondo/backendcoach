package fr.coaching.maseance.service.criteria;

import fr.coaching.maseance.domain.enumeration.CanalSeance;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.coaching.maseance.domain.Disponibilite} entity. This class is used
 * in {@link fr.coaching.maseance.web.rest.DisponibiliteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /disponibilites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DisponibiliteCriteria implements Serializable, Criteria {

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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter heureDebutCreneauxDisponibilite;

    private InstantFilter heureFinCreneauxDisponibilite;

    private CanalSeanceFilter canalSeance;

    private LongFilter coachExpertId;

    private Boolean distinct;

    public DisponibiliteCriteria() {}

    public DisponibiliteCriteria(DisponibiliteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.heureDebutCreneauxDisponibilite = other.optionalHeureDebutCreneauxDisponibilite().map(InstantFilter::copy).orElse(null);
        this.heureFinCreneauxDisponibilite = other.optionalHeureFinCreneauxDisponibilite().map(InstantFilter::copy).orElse(null);
        this.canalSeance = other.optionalCanalSeance().map(CanalSeanceFilter::copy).orElse(null);
        this.coachExpertId = other.optionalCoachExpertId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DisponibiliteCriteria copy() {
        return new DisponibiliteCriteria(this);
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

    public InstantFilter getHeureDebutCreneauxDisponibilite() {
        return heureDebutCreneauxDisponibilite;
    }

    public Optional<InstantFilter> optionalHeureDebutCreneauxDisponibilite() {
        return Optional.ofNullable(heureDebutCreneauxDisponibilite);
    }

    public InstantFilter heureDebutCreneauxDisponibilite() {
        if (heureDebutCreneauxDisponibilite == null) {
            setHeureDebutCreneauxDisponibilite(new InstantFilter());
        }
        return heureDebutCreneauxDisponibilite;
    }

    public void setHeureDebutCreneauxDisponibilite(InstantFilter heureDebutCreneauxDisponibilite) {
        this.heureDebutCreneauxDisponibilite = heureDebutCreneauxDisponibilite;
    }

    public InstantFilter getHeureFinCreneauxDisponibilite() {
        return heureFinCreneauxDisponibilite;
    }

    public Optional<InstantFilter> optionalHeureFinCreneauxDisponibilite() {
        return Optional.ofNullable(heureFinCreneauxDisponibilite);
    }

    public InstantFilter heureFinCreneauxDisponibilite() {
        if (heureFinCreneauxDisponibilite == null) {
            setHeureFinCreneauxDisponibilite(new InstantFilter());
        }
        return heureFinCreneauxDisponibilite;
    }

    public void setHeureFinCreneauxDisponibilite(InstantFilter heureFinCreneauxDisponibilite) {
        this.heureFinCreneauxDisponibilite = heureFinCreneauxDisponibilite;
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
        final DisponibiliteCriteria that = (DisponibiliteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(heureDebutCreneauxDisponibilite, that.heureDebutCreneauxDisponibilite) &&
            Objects.equals(heureFinCreneauxDisponibilite, that.heureFinCreneauxDisponibilite) &&
            Objects.equals(canalSeance, that.canalSeance) &&
            Objects.equals(coachExpertId, that.coachExpertId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, heureDebutCreneauxDisponibilite, heureFinCreneauxDisponibilite, canalSeance, coachExpertId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DisponibiliteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalHeureDebutCreneauxDisponibilite().map(f -> "heureDebutCreneauxDisponibilite=" + f + ", ").orElse("") +
            optionalHeureFinCreneauxDisponibilite().map(f -> "heureFinCreneauxDisponibilite=" + f + ", ").orElse("") +
            optionalCanalSeance().map(f -> "canalSeance=" + f + ", ").orElse("") +
            optionalCoachExpertId().map(f -> "coachExpertId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
