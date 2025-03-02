package catdata.aql;

public enum AqlSyntax {

	CommentExp, ColimSchExpQuotient, ColimSchExpRaw, ColimSchExpVar, ColimSchExpWrap, ColimSchExpModify, EdsExpVar,
	EdsExpRaw, EdsExpSch, GraphExpRaw, GraphExpVar, InstExpAnonymize, InstExpCascadeDelete, InstExpChase, InstExpFrozen,
	InstExpCoEq, InstExpCoEval, InstExpCoProdFull
//	,InstExpCoProdSigma
	, InstExpCod, InstExpColim, InstExpDelta, InstExpDiff, InstExpDistinct, InstExpDom, InstExpEmpty, InstExpEval,
	InstExpPi, InstExpPivot, InstExpSigma, InstExpSigmaChase, InstExpVar, InstExpCsv
	// ,InstExpCsvQuotient
	, InstExpJdbc, InstExpJdbcAll
	// ,InstExpJdbcQuotient
	, InstExpQueryQuotient
	// ,InstExpQuotient
	, InstExpRandom, InstExpRaw, MapExpComp, MapExpId, MapExpPivot, MapExpVar, MapExpColim, MapExpRaw, PragmaExpCheck,
	PragmaExpConsistent, PragmaExpJs, PragmaExpMatch, PragmaExpProc, PragmaExpSql, PragmaExpToCsvInst,
	PragmaExpToCsvTrans, PragmaExpToJdbcInst, PragmaExpToJdbcQuery, PragmaExpToJdbcTrans, PragmaExpVar, PragmaExpCheck2,
	QueryExpFromEds, QueryExpCompose, QueryExpDeltaCoEval, QueryExpDeltaEval, QueryExpId, QueryExpVar, QueryExpRaw,
	QueryExpRawSimple, QueryExpFromCoSpan, SchExpCod, SchExpDom, SchExpSrc, SchExpDst, SchExpEmpty, SchExpInst,
	SchExpPivot, SchExpVar, SchExpColim, SchExpRaw, SchExpJdbcAll, TransExpDiff, TransExpDiffReturn, TransExpCoEval,
	TransExpCoEvalEvalCoUnit, TransExpCoEvalEvalUnit, TransExpDelta, TransExpDistinct, TransExpEval, TransExpId,
	TransExpSigma, TransExpSigmaDeltaCounit, TransExpSigmaDeltaUnit, TransExpVar, TransExpCompose, TransExpCsv,
	TransExpDistinct2, TransExpJdbc, TransExpPi, TransExpRaw, TransExpFrozen, TyExpEmpty, TyExpSch, TyExpVar, TyExpRaw,
	TyExpSql;

}
