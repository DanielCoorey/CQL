package catdata.aql.exp;

import java.util.Map;
import java.util.Set;

import catdata.Util;
import catdata.aql.Kind;
import gnu.trove.set.hash.THashSet;

public class KindCtx<V, G, T, S, I, H, F, Q, P, C, SC, ED> {

	public final Map<V, G> gs = Util.mk();
	public final Map<V, T> tys = Util.mk();
	public final Map<V, S> schs = Util.mk();
	public final Map<V, I> insts = Util.mk();
	public final Map<V, H> trans = Util.mk();
	public final Map<V, F> maps = Util.mk();
	public final Map<V, Q> qs = Util.mk();
	public final Map<V, P> ps = Util.mk();
	public final Map<V, C> cs = Util.mk();
	public final Map<V, SC> scs = Util.mk();
	public final Map<V, ED> eds = Util.mk();
	public final Map<V, Object> tms = Util.mk();

	public Map<V, ?> get(Kind kind) {
		switch (kind) {
		case INSTANCE:
			return insts;
		case MAPPING:
			return maps;
		case SCHEMA:
			return schs;
		case TRANSFORM:
			return trans;
		case TYPESIDE:
			return tys;
		case QUERY:
			return qs;
		case PRAGMA:
			return ps;
		case GRAPH:
			return gs;
		case COMMENT:
			return cs;
		case SCHEMA_COLIMIT:
			return scs;
		case CONSTRAINTS:
			return eds;
		case THEORY_MORPHISM:
			return tms;
		default:
			throw new RuntimeException();
		}
	}

	public Object get(V k, Kind kind) {
		return get(kind).get(k);
	}

	@SuppressWarnings("unchecked")
	public void put(V k, Kind kind, Object o) {
		switch (kind) {
		case INSTANCE:
			insts.put(k, (I) o);
			break;
		case MAPPING:
			maps.put(k, (F) o);
			break;
		case SCHEMA:
			schs.put(k, (S) o);
			break;
		case TRANSFORM:
			trans.put(k, (H) o);
			break;
		case TYPESIDE:
			tys.put(k, (T) o);
			break;
		case QUERY:
			qs.put(k, (Q) o);
			break;
		case PRAGMA:
			ps.put(k, (P) o);
			break;
		case GRAPH:
			gs.put(k, (G) o);
			break;
		case COMMENT:
			cs.put(k, (C) o);
			break;
		case SCHEMA_COLIMIT:
			scs.put(k, (SC) o);
			break;
		case CONSTRAINTS:
			eds.put(k, (ED) o);
			break;
		case THEORY_MORPHISM:
			tms.put(k, o);
		default:
			throw new RuntimeException();
		}
	}

	public Set<V> keySet() {
		Set<V> ret = new THashSet<>();
		ret.addAll(insts.keySet());
		ret.addAll(maps.keySet());
		ret.addAll(ps.keySet());
		ret.addAll(qs.keySet());
		ret.addAll(schs.keySet());
		ret.addAll(trans.keySet());
		ret.addAll(tys.keySet());
		ret.addAll(qs.keySet());
		ret.addAll(gs.keySet());
		ret.addAll(cs.keySet());
		ret.addAll(scs.keySet());
		ret.addAll(eds.keySet());
		return ret;
	}

	public int size(Kind k) {
		switch (k) {
		case COMMENT:
			return cs.size();
		case CONSTRAINTS:
			return eds.size();
		case GRAPH:
			return gs.size();
		case INSTANCE:
			return insts.size();
		case MAPPING:
			return maps.size();
		case PRAGMA:
			return ps.size();
		case QUERY:
			return qs.size();
		case SCHEMA:
			return schs.size();
		case SCHEMA_COLIMIT:
			return scs.size();
		case TRANSFORM:
			return trans.size();
		case TYPESIDE:
			return tys.size();
		case THEORY_MORPHISM:
			return tms.size();

		}
		return Util.anomaly();
	}

	@Override
	public String toString() {
		return "KindCtx [gs=" + gs + ", tys=" + tys + ", schs=" + schs + ", insts=" + insts + ", trans=" + trans
				+ ", maps=" + maps + ", qs=" + qs + ", ps=" + ps + ", cs=" + cs + ", scs=" + scs + ", eds=" + eds + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cs == null) ? 0 : cs.hashCode());
		result = prime * result + ((eds == null) ? 0 : eds.hashCode());
		result = prime * result + ((gs == null) ? 0 : gs.hashCode());
		result = prime * result + ((insts == null) ? 0 : insts.hashCode());
		result = prime * result + ((maps == null) ? 0 : maps.hashCode());
		result = prime * result + ((ps == null) ? 0 : ps.hashCode());
		result = prime * result + ((qs == null) ? 0 : qs.hashCode());
		result = prime * result + ((schs == null) ? 0 : schs.hashCode());
		result = prime * result + ((scs == null) ? 0 : scs.hashCode());
		result = prime * result + ((trans == null) ? 0 : trans.hashCode());
		result = prime * result + ((tys == null) ? 0 : tys.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KindCtx other = (KindCtx) obj;
		if (cs == null) {
			if (other.cs != null)
				return false;
		} else if (!cs.equals(other.cs))
			return false;
		if (eds == null) {
			if (other.eds != null)
				return false;
		} else if (!eds.equals(other.eds))
			return false;
		if (gs == null) {
			if (other.gs != null)
				return false;
		} else if (!gs.equals(other.gs))
			return false;
		if (insts == null) {
			if (other.insts != null)
				return false;
		} else if (!insts.equals(other.insts))
			return false;
		if (maps == null) {
			if (other.maps != null)
				return false;
		} else if (!maps.equals(other.maps))
			return false;
		if (ps == null) {
			if (other.ps != null)
				return false;
		} else if (!ps.equals(other.ps))
			return false;
		if (qs == null) {
			if (other.qs != null)
				return false;
		} else if (!qs.equals(other.qs))
			return false;
		if (schs == null) {
			if (other.schs != null)
				return false;
		} else if (!schs.equals(other.schs))
			return false;
		if (scs == null) {
			if (other.scs != null)
				return false;
		} else if (!scs.equals(other.scs))
			return false;
		if (trans == null) {
			if (other.trans != null)
				return false;
		} else if (!trans.equals(other.trans))
			return false;
		if (tys == null) {
			if (other.tys != null)
				return false;
		} else if (!tys.equals(other.tys))
			return false;
		return true;
	}
	
	
}
