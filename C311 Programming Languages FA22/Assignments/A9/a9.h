void *r__t__expr__t__, *r__t__envr__m__cpsr__t__, *r__t__kr__t__, *r__t__yr__t__, *r__t__ratorr__t__, *r__t__argr__t__, *r__t__vr__t__;

void (*r__t__pcr__t__)();

struct expr;
typedef struct expr expr;
struct expr {
  enum {
    _const_expr,
    _var_expr,
    _subr1_expr,
    _zero_expr,
    _mult_expr,
    _if_expr,
    _letcc_expr,
    _throw_expr,
    _let_expr,
    _lambda_expr,
    _app_expr
  } tag;
  union {
    struct { void *_cexp; } _const;
    struct { void *_n; } _var;
    struct { void *_nexp; } _subr1;
    struct { void *_nexp; } _zero;
    struct { void *_nexpr1; void *_nexpr2; } _mult;
    struct { void *_test; void *_conseq; void *_alt; } _if;
    struct { void *_body; } _letcc;
    struct { void *_kexp; void *_vexp; } _throw;
    struct { void *_exp; void *_body; } _let;
    struct { void *_body; } _lambda;
    struct { void *_rator; void *_rand; } _app;
  } u;
};

void *exprr_const(void *cexp);
void *exprr_var(void *n);
void *exprr_subr1(void *nexp);
void *exprr_zero(void *nexp);
void *exprr_mult(void *nexpr1, void *nexpr2);
void *exprr_if(void *test, void *conseq, void *alt);
void *exprr_letcc(void *body);
void *exprr_throw(void *kexp, void *vexp);
void *exprr_let(void *exp, void *body);
void *exprr_lambda(void *body);
void *exprr_app(void *rator, void *rand);

void valuer__m__ofr__m__cps();
struct envr;
typedef struct envr envr;
struct envr {
  enum {
    _emptyr__m__env_envr,
    _extendr__m__env_envr
  } tag;
  union {
    struct { char dummy; } _emptyr__m__env;
    struct { void *_envr__m__cpsr__ex__; void *_k; } _extendr__m__env;
  } u;
};

void *envrr_emptyr__m__env();
void *envrr_extendr__m__env(void *envr__m__cpsr__ex__, void *k);

void applyr__m__env();
struct clos;
typedef struct clos clos;
struct clos {
  enum {
    _make_clos
  } tag;
  union {
    struct { void *_body; void *_envr__m__cps; void *_k; } _make;
  } u;
};

void *closr_make(void *body, void *envr__m__cps, void *k);

void applyr__m__clos();
struct kt;
typedef struct kt kt;
struct kt {
  enum {
    _emptyr__m__k_kt,
    _maker__m__subr1r__m__k_kt,
    _maker__m__zeror__m__k_kt,
    _maker__m__throwr__m__k_kt,
    _maker__m__letr__m__k_kt,
    _maker__m__randr__m__k_kt,
    _maker__m__ratorr__m__k_kt,
    _maker__m__multr__m__innerr__m__k_kt,
    _maker__m__multr__m__outerr__m__k_kt,
    _maker__m__ifr__m__k_kt
  } tag;
  union {
    struct { void *_jumpout; } _emptyr__m__k;
    struct { void *_k; } _maker__m__subr1r__m__k;
    struct { void *_k; } _maker__m__zeror__m__k;
    struct { void *_vr__m__exp; void *_envr__m__cps; } _maker__m__throwr__m__k;
    struct { void *_body; void *_envr__m__cps; void *_k; } _maker__m__letr__m__k;
    struct { void *_vr__m__rator; void *_k; } _maker__m__randr__m__k;
    struct { void *_rand; void *_envr__m__cps; void *_k; } _maker__m__ratorr__m__k;
    struct { void *_w; void *_k; } _maker__m__multr__m__innerr__m__k;
    struct { void *_xr2; void *_envr__m__cps; void *_k; } _maker__m__multr__m__outerr__m__k;
    struct { void *_conseq; void *_alt; void *_envr__m__cps; void *_k; } _maker__m__ifr__m__k;
  } u;
};

void *ktr_emptyr__m__k(void *jumpout);
void *ktr_maker__m__subr1r__m__k(void *k);
void *ktr_maker__m__zeror__m__k(void *k);
void *ktr_maker__m__throwr__m__k(void *vr__m__exp, void *envr__m__cps);
void *ktr_maker__m__letr__m__k(void *body, void *envr__m__cps, void *k);
void *ktr_maker__m__randr__m__k(void *vr__m__rator, void *k);
void *ktr_maker__m__ratorr__m__k(void *rand, void *envr__m__cps, void *k);
void *ktr_maker__m__multr__m__innerr__m__k(void *w, void *k);
void *ktr_maker__m__multr__m__outerr__m__k(void *xr2, void *envr__m__cps, void *k);
void *ktr_maker__m__ifr__m__k(void *conseq, void *alt, void *envr__m__cps, void *k);

void applyr__m__k();
int main();
int mount_tram();

struct _trstr;
typedef struct _trstr _trstr;
struct _trstr {
  jmp_buf *jmpbuf;
  int value;
};

