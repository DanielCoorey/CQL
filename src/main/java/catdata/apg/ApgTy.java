package catdata.apg;

import java.util.Map;

import catdata.Util;
import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.HashingStrategy;

public class ApgTy<F,L,B> {

	public final Map<F,ApgTy<F,L,B>> m;
	public final boolean all;

	public final L l;
	public final B b;
	
	@SuppressWarnings("rawtypes")
	private static HashingStrategy<ApgTy> strategy = new HashingStrategy<>() {
		private static final long serialVersionUID = 1L;

		@Override
		public int computeHashCode(ApgTy t) {
			return t.hashCode2();
		}

		@Override
		public boolean equals(ApgTy s, ApgTy t) {
			return s.equals2(t);
		}
	};

	@SuppressWarnings("rawtypes")
	public static Map<ApgTy, ApgTy> cache = new TCustomHashMap<>(strategy);

	
	public static synchronized <F,L,B> ApgTy<F,L,B> ApgTyL(L str) {
		return mkApgTy(str, null, false, null);
	}
	
	public static synchronized <F,L,B> ApgTy<F,L,B> ApgTyB(B str) {
		return mkApgTy(null, str, false, null);
	}
	
	public static synchronized <F,L,B> ApgTy<F,L,B> ApgTyP(boolean all, Map<F,ApgTy<F,L,B>> str) {
		return mkApgTy(null, null, all, str);
	}

	
	private static synchronized <F,L,B> ApgTy<F,L,B> mkApgTy(L l, B b, boolean all, Map<F,ApgTy<F,L,B>> m) {
		ApgTy<F,L,B> ret = new ApgTy<>(l, b, all, m);
		
		ApgTy<F,L,B> ret2 = cache.get(ret);
		if (ret2 != null) {
			return ret2;
		}
		cache.put(ret, ret);
		return ret;
	}
	
	private ApgTy(L l, B b, boolean all, Map<F,ApgTy<F,L,B>> m) {
		this.l = l;
		this.b = b;
		this.all = all;
		this.m = m;
	}
	

	@Override
	public int hashCode() {
		return System.identityHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this;
	} 

	public int hashCode2() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (all ? 1231 : 1237);
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + ((l == null) ? 0 : l.hashCode());
		result = prime * result + ((m == null) ? 0 : m.hashCode());
		return result;
	}


	public boolean equals2(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApgTy other = (ApgTy) obj;
		if (all != other.all)
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		if (l == null) {
			if (other.l != null)
				return false;
		} else if (!l.equals(other.l))
			return false;
		if (m == null) {
			if (other.m != null)
				return false;
		} else if (!m.equals(other.m))
			return false;
		return true;
	}


	@Override
	public String toString() {
		if (l != null) {
			return l.toString();
		} else if (b != null) {
			return b.toString();
		}
		if (all) {
			return "(" + Util.sep(m, ": ", ", ") + ")";
		}
		return "<" + Util.sep(m, ": ", ", ") + ">";
	}
}
