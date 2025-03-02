package catdata.aql.fdm;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.IntConsumer;

import com.google.common.collect.Iterators;

import catdata.Chc;
import catdata.Pair;
import catdata.Triple;
import catdata.Util.UpTo;
import catdata.aql.Instance;
import catdata.aql.Mapping;
import catdata.aql.Term;
import catdata.aql.Var;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

public class Chase<Ty, En1, Sym, Fk1, Att1, En2, Fk2, Att2, Gen, Sk, X, Y> {

	final Mapping<Ty, En1, Sym, Fk1, Att1, En2, Fk2, Att2> F;

	private Instance<Ty, En1, Sym, Fk1, Att1, Gen, Sk, X, Y> I;

	public Collection<Integer> en(En2 en2, int o) {
		En2Stuff x = stuff.get(en2);
		// BitSet set = x.ens;
		int top = x.top;
		int size = x.size();
		return new AbstractCollection<>() {
			@Override
			public Iterator<Integer> iterator() {
				return Iterators.filter(new UpTo(o, o + top), z -> x.find(z - o) == z - o);
			}

			@Override
			public int size() {
				return size;
			}
		};
	}

	public static class BST {
		Set<Integer> set;
		public final int node;
		// true if added

		public boolean add(int n) {
			if (node == n) {
				return false;
			}
			if (set == null) {
				set = new TreeSet<>();
			}
			return set.add(n);
		}

		public BST(int n) {
			node = n;
		}

		public void foreach(IntConsumer f) {
			f.accept(node);
			if (set == null) {
				return;
			}
			for (Integer x : set) {
				f.accept(x);
			}
		}

		public void foreachNoRoot(IntConsumer f) {
			if (set == null) {
				return;
			}
			for (Integer x : set) {
				f.accept(x);
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + node;
			result = prime * result + ((set == null) ? 0 : set.hashCode());
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
			BST other = (BST) obj;
			if (node != other.node)
				return false;
			if (set == null) {
				if (other.set != null)
					return false;
			} else if (!set.equals(other.set))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "BST [set=" + set + ", node=" + node + "]";
		}

	}

	class En2Stuff {
		public int top;
		public int[] parent;
		public Term<Void, En2, Void, Fk2, Void, Gen, Void>[] iso1;
		public TObjectIntMap<Term<Void, En2, Void, Fk2, Void, Gen, Void>> iso2;
		// public Map<Term<Void, En2, Void, Fk2, Void, Gen, Void>, Integer> iso2;
		public En2 en2;
		public Map<Fk2, BST[]> fks;

		@SuppressWarnings("unchecked")
		public En2Stuff(int m, En2 en) {
			top = 0;
			en2 = en;
			parent = new int[m];
			iso1 = new Term[m];
			iso2 = new TObjectIntHashMap<>(m);
			// iso2 = new THashMap<>(m);
			fks = new THashMap<>();
			for (Fk2 fk2 : F.dst.fksFrom(en2)) {
				fks.put(fk2, new BST[m]);
			}
		}

		@SuppressWarnings("unchecked")
		public void add(Term<Void, En2, Void, Fk2, Void, Gen, Void> u) {
			parent[top] = top;
			iso1[top] = u;
			iso2.put(u, top);
			top++;
			if (!(top < parent.length)) {
				int[] parent2 = parent;
				Term<Void, En2, Void, Fk2, Void, Gen, Void>[] isoX = iso1;
				parent = new int[parent2.length * 2];
				iso1 = new Term[parent2.length * 2];
				System.arraycopy(parent2, 0, parent, 0, parent2.length);
				System.arraycopy(isoX, 0, iso1, 0, parent2.length);

				fks.replaceAll((fk, arr) -> {
					BST[] arr2 = new BST[parent2.length * 2];
					System.arraycopy(arr, 0, arr2, 0, parent2.length);
					return arr2;
				});
			}
		}

		public int size() {
			int x = 0;
			for (int i = 0; i < top; i++) {
				if (find(i) == i) {
					x++;
				}
			}
			return x;
		}

		public void merge() {
			for (Fk2 fk2 : fks.keySet()) {
				En2Stuff en2 = stuff.get(F.dst.fks.get(fk2).second);
				BST[] set = fks.get(fk2);
				for (int i = 0; i < top; i++) {
					BST b = set[i];
					if (b == null) {
						continue;
					}
					int j = find(i);

					if (j == i) {
						// required for correctness
						BST y = new BST(b.node);
						set[j] = y;
						b.foreachNoRoot(q -> y.add(en2.find(q)));
					} else {
						BST y = set[j];
						if (y == null) {
							y = new BST(b.node);
							set[j] = y;
						}
						b.foreach(q -> set[j].add(en2.find(q)));
						set[i] = null;
						// iso2 still has stale ref, ditto iso1
						// iso1[i] = null;
					}
				}
			}
		}

		public int find(int p) {
			int root = p;
			while (root != parent[root]) {
				root = parent[root];
			}
			while (p != root) {
				int newp = parent[p];
				parent[p] = root;
				p = newp;
			}
			return root;
		}

		@Override
		public String toString() {
			String fks0 = "";
			for (Fk2 fk2 : fks.keySet()) {
				fks0 += fk2 + ": " + Arrays.toString(fks.get(fk2)) + "\n";
			}
			return "En2Stuff [top=" + top + ", parent=" + Arrays.toString(parent) + ", iso1=" + Arrays.toString(iso1)
					+ ", iso2=" + iso2 + ", en2=" + en2 + ", fks=" + fks0 + "]";
		}

	}

	public Map<En2, En2Stuff> stuff = new THashMap<>();

	public Map<En1, Set<Pair<X, X>>> extra;

	public Chase(Mapping<Ty, En1, Sym, Fk1, Att1, En2, Fk2, Att2> F, Instance<Ty, En1, Sym, Fk1, Att1, Gen, Sk, X, Y> I,
			Map<En1, Set<Pair<X, X>>> extra) {
		this.F = F;
		this.I = I;
		this.extra = extra;
		Map<En2, Integer> m = new THashMap<>();
		for (En2 en2 : F.dst.ens) {
			m.put(en2, 0);
		}
		for (En1 en1 : I.schema().ens) {
			En2 en2 = F.ens.get(en1);
			int x = I.algebra().size(en1);
			m.put(en2, m.get(en2) + x);
		}
		Map<En2, Integer> mm = new THashMap<>(m);
		for (Fk2 fk2 : F.dst.fks.keySet()) {
			Pair<En2, En2> x = F.dst.fks.get(fk2);
			mm.put(x.second, mm.get(x.second) + m.get(x.first));
		}
		stuff = new THashMap<>();
		for (En2 en2 : F.dst.ens) {
			stuff.put(en2, new En2Stuff(2 * mm.get(en2), en2));
		}
		for (Entry<Gen, En1> x : I.gens().entrySet()) {
			stuff.get(F.ens.get(x.getValue())).add(Term.Gen(x.getKey()));
		}
		while (step()) {
			// System.out.println("** " + this);
			System.gc();
			// System.runFinalization();
		}
	}

	private volatile boolean changed;

	Map<En2, Integer> sizes = new THashMap<>();
	boolean first = true;

	private synchronized boolean step() {
		changed = false;
		// System.out.println("0 " + changed + " " + toString());
		if (!first) {
			makeArrowsTotal();
		} else {
			makeArrowsTotalFirst();
			first = false;
		}
		// System.out.println("A " + changed + " " + toString());
		// makeObjectsTotal();
		// System.out.println("B " + changed+ " " + toString());
		collageEqs();
		// System.out.println("C " + changed + " " + toString());
		targetEqs();
		// System.out.println("D " + changed + " " + toString());
		doExtra();
		// System.out.println("E " + changed + " " + toString());
		makeFunctional();
		// .out.println("F " + changed + " " + toString());

		for (En2 en2 : F.dst.ens) {
			stuff.get(en2).merge();
		}
		// System.out.println("F " + changed + " " + toString());

		return changed;
	}

	public synchronized void makeArrowsTotalFirst() {
		Map<Fk2, BitSet> ctx = new THashMap<>();
		for (Fk2 fk2 : F.dst.fks.keySet()) {
			ctx.put(fk2, new BitSet(stuff.get(F.dst.fks.get(fk2).first).top));
		}
		for (En2 v : F.dst.ens) {
			En2Stuff T_v = stuff.get(v);
			for (Fk2 a : F.dst.fksFrom(v)) {
				BST[] T_a = stuff.get(v).fks.get(a);
				for (int x = 0; x < stuff.get(v).top; x++) {
					if (x != T_v.find(x) || T_a[x] != null) {
						continue;
					}
					ctx.get(a).set(stuff.get(v).find(x), true);
				}
			}
		}
		for (Fk2 a : F.dst.fks.keySet()) {
			BitSet set = ctx.get(a);
			En2 u = F.dst.fks.get(a).second;
			En2 vv = F.dst.fks.get(a).first;
			for (int x = set.nextSetBit(0); x >= 0; x = set.nextSetBit(x + 1)) {
				Term<Void, En2, Void, Fk2, Void, Gen, Void> z = stuff.get(vv).iso1[x];
				Term<Void, En2, Void, Fk2, Void, Gen, Void> y = Term.Fk(a, z);
				BST q = stuff.get(vv).fks.get(a)[x];
				if (q == null) {
					stuff.get(vv).fks.get(a)[x] = new BST(stuff.get(u).iso2.get(find(y, u)));
					changed = true;
					continue;
				}
				changed = changed | q.add(stuff.get(u).iso2.get(find(y, u)));
			}
		}
	}

	public synchronized void makeArrowsTotal() {
		Map<Fk2, BST> ctx = new THashMap<>();
		for (En2 v : F.dst.ens) {
			En2Stuff T_v = stuff.get(v);
			for (Fk2 a : F.dst.fksFrom(v)) {
				BST[] T_a = stuff.get(v).fks.get(a);
				for (int x = 0; x < stuff.get(v).top; x++) {
					if (x != T_v.find(x)) {
						continue;
					}
					if (T_a[x] == null) {
						BST b = ctx.get(a);
						if (b != null) {
							b.add(stuff.get(v).find(x));
						} else {
							ctx.put(a, new BST(stuff.get(v).find(x)));
						}
					}
				}
			}
		}
		for (Fk2 a : ctx.keySet()) {
			BST set = ctx.get(a);
			En2 u = F.dst.fks.get(a).second;
			En2 vv = F.dst.fks.get(a).first;
			set.foreach(x -> {
				Term<Void, En2, Void, Fk2, Void, Gen, Void> z = stuff.get(vv).iso1[x];
				Term<Void, En2, Void, Fk2, Void, Gen, Void> y = Term.Fk(a, z);
				BST q = stuff.get(vv).fks.get(a)[x];
				if (q == null) {
					stuff.get(vv).fks.get(a)[x] = new BST(stuff.get(u).iso2.get(find(y, u)));
					changed = true;
					return;
				}
				changed = changed | q.add(stuff.get(u).iso2.get(find(y, u)));
			});
		}
	}

	// need f(x,y) /\ f(x',y') /\ x ~ x' -> y ~ y'
	public synchronized void makeFunctional() {
		for (En2 v : F.dst.ens) {
			for (Fk2 a : F.dst.fksFrom(v)) {
				En2 en2 = F.dst.fks.get(a).second;
				BST[] T_a = stuff.get(v).fks.get(a);
				for (int x = 0; x < stuff.get(v).top; x++) {
					if (stuff.get(v).find(x) != x) {
						continue;
					}
					BST ys = T_a[x];
					if (ys == null) {
						continue;
					}
					int y1 = ys.node;
					ys.foreachNoRoot(y2x -> {
						if (y1 != y2x) {
							changed = changed | union(y1, y2x, en2);
						}
					});
				}
			}
		}
	}

	// enough to do just for generators?
	public synchronized void collageEqs() {
		for (En1 en : I.schema().ens) {
			for (X x : I.algebra().en(en)) {
				// Term<Void, En1, Void, Fk1, Void, Gen, Void> t = I.algebra().repr(en, x);
				// Term<Void, En2, Void, Fk2, Void, Gen, Void> u =
				// F.trans(t.convert()).convert();
				Term<Void, En2, Void, Fk2, Void, Gen, Void> Fx = F.trans(I.algebra().repr(en, x).convert()).convert();
				for (Fk1 a : F.src.fksFrom(en)) {
					Term<Void, En2, Void, Fk2, Void, Gen, Void> lhs = F
							.trans(I.algebra().repr(I.schema().fks.get(a).second, I.algebra().fk(a, x)).convert())
							.convert();
					En2 en2 = F.ens.get(F.src.fks.get(a).first);
					En2 en3 = F.ens.get(F.src.fks.get(a).second);
					evalX(F.fks.get(a).second, stuff.get(en2).find(stuff.get(en2).iso2.get(Fx)), m -> {
						changed = changed | union(m, stuff.get(en3).find(stuff.get(en3).iso2.get(lhs)), en3);
					});
				}
			}
		}
	}

	public synchronized void doExtra() {
		for (En1 en1 : extra.keySet()) {
			En2 en2 = F.ens.get(en1);
			for (Pair<X, X> x : extra.get(en1)) {
				Term<Void, En1, Void, Fk1, Void, Gen, Void> t = I.algebra().repr(en1, x.first);
				Term<Void, En2, Void, Fk2, Void, Gen, Void> u = F.trans(t.convert()).convert();

				Term<Void, En1, Void, Fk1, Void, Gen, Void> t0 = I.algebra().repr(en1, x.second);
				Term<Void, En2, Void, Fk2, Void, Gen, Void> u0 = F.trans(t0.convert()).convert();

				Term<Void, En2, Void, Fk2, Void, Gen, Void> a = find(u, en2);
				Term<Void, En2, Void, Fk2, Void, Gen, Void> b = find(u0, en2);
				if (!a.equals(b)) {
					changed = true;
					union(a, b, en2);
				}
			}
		}
	}

	public synchronized void targetEqs() {
		for (Triple<Pair<Var, En2>, Term<Ty, En2, Sym, Fk2, Att2, Void, Void>, Term<Ty, En2, Sym, Fk2, Att2, Void, Void>> eq : F.dst.eqs) {
			Chc<Ty, En2> t = F.dst.type(eq.first, eq.second);
			if (t.left) {
				continue;
			}
			En2 src = eq.first.second;
			List<Fk2> lhs = eq.second.toFkList();
			List<Fk2> rhs = eq.third.toFkList();

			for (int initial = 0; initial < stuff.get(src).top; initial++) {
				if (stuff.get(src).find(initial) != initial) {
					continue;
				}
				List<Integer> l = new LinkedList<>();
				evalX(lhs, initial, l::add);
				evalX(rhs, initial, i -> {
					for (Integer n : l) {
						// System.out.println("gire");
						changed = changed | union(n, i, t.r);
					}
				});

			}
		}
	}

	public void evalX(List<Fk2> lhs, int n, IntConsumer f) {
		if (lhs.isEmpty()) {
			f.accept(n);
			return;
		}
		Fk2 fk = lhs.get(0);
		// find req correctness
		BST s = stuff.get(F.dst.fks.get(fk).first).fks.get(fk)[stuff.get(F.dst.fks.get(fk).first).find(n)];
		if (s != null) {
			s.foreach(z -> {
				evalX(lhs.subList(1, lhs.size()), z, f);
			});
		}
	}

	private synchronized void add(Term<Void, En2, Void, Fk2, Void, Gen, Void> x, En2 en2) {
		if (stuff.get(en2).iso2.containsKey(x)) {
			return;
		}
		stuff.get(en2).add(x);
	}

	private synchronized boolean union(int p, int q, En2 en2) {
		int rootP = stuff.get(en2).find(p);
		int rootQ = stuff.get(en2).find(q);
		if (rootP == rootQ) {
			return false;
		} else if (rootP > rootQ) {
			stuff.get(en2).parent[rootP] = rootQ;
		} else {
			stuff.get(en2).parent[rootQ] = rootP;
		}
		return true;
	}

	public synchronized Term<Void, En2, Void, Fk2, Void, Gen, Void> find(Term<Void, En2, Void, Fk2, Void, Gen, Void> p,
			En2 en2) {
		add(p, en2);
		return stuff.get(en2).iso1[stuff.get(en2).find(stuff.get(en2).iso2.get(p))];
	}

	public synchronized Term<Void, En2, Void, Fk2, Void, Gen, Void> findNoAdd(
			Term<Void, En2, Void, Fk2, Void, Gen, Void> p, En2 en2) {
		return stuff.get(en2).iso1[stuff.get(en2).find(stuff.get(en2).iso2.get(p))];
	}

	public synchronized boolean connected(Term<Void, En2, Void, Fk2, Void, Gen, Void> p,
			Term<Void, En2, Void, Fk2, Void, Gen, Void> q, En2 en2) {
		add(p, en2);
		add(q, en2);
		int pp = stuff.get(en2).iso2.get(p);
		int qq = stuff.get(en2).iso2.get(q);
		int a = stuff.get(en2).find(pp);
		int b = stuff.get(en2).find(qq);
		return a == b;
	}

	public synchronized void union(Term<Void, En2, Void, Fk2, Void, Gen, Void> p,
			Term<Void, En2, Void, Fk2, Void, Gen, Void> q, En2 en2) {
		add(p, en2);
		add(q, en2);
		union(stuff.get(en2).iso2.get(p), stuff.get(en2).iso2.get(q), en2);
	}

	@Override
	public String toString() {
		return "Chase [stuff=" + stuff + "]";
	}

}
