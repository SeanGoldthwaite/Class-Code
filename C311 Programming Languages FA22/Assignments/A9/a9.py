import sys
from enum import Enum
from greenlet import greenlet
from typing import Any, Callable

# Define the registers
g__exp_: object = None
g__env_cps_: object = None
g__k_: object = None
g__y_: object = None
g__rator_: object = None
g__arg_: object = None
g__v_: object = None

# Define the program counter
g__pc_ : Callable[[], Any] = None

# Define the dismount greenlet
_dismount_blank = None

# Define the union classes
class union_t(object):
    class expr(Enum):
        const = 0
        var = 1
        subr1 = 2
        zero = 3
        mult = 4
        _if = 5
        letcc = 6
        throw = 7
        let = 8
        _lambda = 9
        app = 10

    class envr(Enum):
        empty_env = 0
        extend_env = 1

    class clos(Enum):
        make = 0

    class kt(Enum):
        empty_k = 0
        make_subr1_k = 1
        make_zero_k = 2
        make_throw_k = 3
        make_let_k = 4
        make_rand_k = 5
        make_rator_k = 6
        make_mult_inner_k = 7
        make_mult_outer_k = 8
        make_if_k = 9

    def __init__(self, type: Enum, **kwargs):
        self.type = type
        for key in kwargs:
            setattr(self, key, kwargs[key])


# Union functions
def kt_empty_k(jumpout):
    return union_t(union_t.kt.empty_k, 
            jumpout=jumpout)

def kt_make_subr1_k(k):
    return union_t(union_t.kt.make_subr1_k, 
            k=k)

def kt_make_zero_k(k):
    return union_t(union_t.kt.make_zero_k, 
            k=k)

def kt_make_throw_k(v_exp, env_cps):
    return union_t(union_t.kt.make_throw_k, 
            v_exp=v_exp, 
            env_cps=env_cps)

def kt_make_let_k(body, env_cps, k):
    return union_t(union_t.kt.make_let_k, 
            body=body, 
            env_cps=env_cps, 
            k=k)

def kt_make_rand_k(v_rator, k):
    return union_t(union_t.kt.make_rand_k, 
            v_rator=v_rator, 
            k=k)

def kt_make_rator_k(rand, env_cps, k):
    return union_t(union_t.kt.make_rator_k, 
            rand=rand, 
            env_cps=env_cps, 
            k=k)

def kt_make_mult_inner_k(w, k):
    return union_t(union_t.kt.make_mult_inner_k, 
            w=w, 
            k=k)

def kt_make_mult_outer_k(xr2, env_cps, k):
    return union_t(union_t.kt.make_mult_outer_k, 
            xr2=xr2, 
            env_cps=env_cps, 
            k=k)

def kt_make_if_k(conseq, alt, env_cps, k):
    return union_t(union_t.kt.make_if_k, 
            conseq=conseq, 
            alt=alt, 
            env_cps=env_cps, 
            k=k)

def clos_make(body, env_cps, k):
    return union_t(union_t.clos.make, 
            body=body, 
            env_cps=env_cps, 
            k=k)

def envr_empty_env():
    return union_t(union_t.envr.empty_env, )

def envr_extend_env(env_cps_cap, k):
    return union_t(union_t.envr.extend_env, 
            env_cps_cap=env_cps_cap, 
            k=k)

def expr_const(cexp):
    return union_t(union_t.expr.const, 
            cexp=cexp)

def expr_var(n):
    return union_t(union_t.expr.var, 
            n=n)

def expr_subr1(nexp):
    return union_t(union_t.expr.subr1, 
            nexp=nexp)

def expr_zero(nexp):
    return union_t(union_t.expr.zero, 
            nexp=nexp)

def expr_mult(nexpr1, nexpr2):
    return union_t(union_t.expr.mult, 
            nexpr1=nexpr1, 
            nexpr2=nexpr2)

def expr_if(test, conseq, alt):
    return union_t(union_t.expr._if, 
            test=test, 
            conseq=conseq, 
            alt=alt)

def expr_letcc(body):
    return union_t(union_t.expr.letcc, 
            body=body)

def expr_throw(kexp, vexp):
    return union_t(union_t.expr.throw, 
            kexp=kexp, 
            vexp=vexp)

def expr_let(exp, body):
    return union_t(union_t.expr.let, 
            exp=exp, 
            body=body)

def expr_lambda(body):
    return union_t(union_t.expr._lambda, 
            body=body)

def expr_app(rator, rand):
    return union_t(union_t.expr.app, 
            rator=rator, 
            rand=rand)

# Generate functions
def apply_k():
    global g__pc_
    global g__env_cps_
    global g__exp_
    global g__k_
    global g__v_
    global g__arg_
    global g__rator_

    match g__k_.type:
        case union_t.kt.empty_k:
            jumpout = g__k_.jumpout
            jumpout.switch()

        case union_t.kt.make_subr1_k:
            k_cap = g__k_.k
            g__k_ = k_cap
            g__v_ = (g__v_ - 1)
            g__pc_ = apply_k

        case union_t.kt.make_zero_k:
            k_cap = g__k_.k
            g__k_ = k_cap
            g__v_ = (g__v_ == 0)
            g__pc_ = apply_k

        case union_t.kt.make_throw_k:
            v_exp_cap = g__k_.v_exp
            env_cps_cap = g__k_.env_cps
            g__k_ = g__v_
            g__exp_ = v_exp_cap
            g__env_cps_ = env_cps_cap
            g__pc_ = value_of_cps

        case union_t.kt.make_rand_k:
            v_rator_cap = g__k_.v_rator
            k_cap = g__k_.k
            g__k_ = k_cap
            g__rator_ = v_rator_cap
            g__arg_ = g__v_
            g__pc_ = apply_clos

        case union_t.kt.make_rator_k:
            rand_cap = g__k_.rand
            env_cps_cap = g__k_.env_cps
            k_cap = g__k_.k
            g__k_ = kt_make_rand_k(g__v_, k_cap)
            g__exp_ = rand_cap
            g__env_cps_ = env_cps_cap
            g__pc_ = value_of_cps

        case union_t.kt.make_mult_inner_k:
            w_cap = g__k_.w
            k_cap = g__k_.k
            g__k_ = k_cap
            g__v_ = g__v_ * w_cap
            g__pc_ = apply_k

        case union_t.kt.make_mult_outer_k:
            xr2_cap = g__k_.xr2
            env_cps_cap = g__k_.env_cps
            k_cap = g__k_.k
            g__k_ = kt_make_mult_inner_k(g__v_, k_cap)
            g__exp_ = xr2_cap
            g__env_cps_ = env_cps_cap
            g__pc_ = value_of_cps

        case union_t.kt.make_let_k:
            body_cap = g__k_.body
            env_cps_cap = g__k_.env_cps
            k_cap = g__k_.k
            g__exp_ = body_cap
            g__env_cps_ = envr_extend_env(env_cps_cap, g__v_)
            g__k_ = k_cap
            g__pc_ = value_of_cps

        case union_t.kt.make_if_k:
            conseq_cap = g__k_.conseq
            alt_cap = g__k_.alt
            env_cps_cap = g__k_.env_cps
            k_cap = g__k_.k
            if g__v_:
                g__k_ = k_cap
                g__exp_ = conseq_cap
                g__env_cps_ = env_cps_cap
                g__pc_ = value_of_cps
            else:
                g__k_ = k_cap
                g__exp_ = alt_cap
                g__env_cps_ = env_cps_cap
                g__pc_ = value_of_cps

def apply_clos():
    global g__pc_
    global g__env_cps_
    global g__arg_
    global g__exp_

    match g__rator_.type:
        case union_t.clos.make:
            body = g__rator_.body
            env_cps = g__rator_.env_cps
            k_cap = g__rator_.k
            g__exp_ = body
            g__env_cps_ = envr_extend_env(env_cps, g__arg_)
            g__pc_ = value_of_cps

def apply_env():
    global g__pc_
    global g__y_
    global g__env_cps_
    global g__v_

    match g__env_cps_.type:
        case union_t.envr.empty_env:
            raise RuntimeError("unbound identifier")

        case union_t.envr.extend_env:
            env_cps_cap = g__env_cps_.env_cps_cap
            k = g__env_cps_.k
            if (g__y_ == 0):
                g__v_ = k
                g__pc_ = apply_k
            else:
                g__env_cps_ = env_cps_cap
                g__y_ = (g__y_ - 1)
                g__pc_ = apply_env

def value_of_cps():
    global g__pc_
    global g__exp_
    global g__k_
    global g__env_cps_
    global g__v_
    global g__y_

    match g__exp_.type:
        case union_t.expr.const:
            num = g__exp_.cexp
            g__v_ = num
            g__pc_ = apply_k

        case union_t.expr.var:
            y = g__exp_.n
            g__y_ = y
            g__pc_ = apply_env

        case union_t.expr.subr1:
            x = g__exp_.nexp
            g__k_ = kt_make_subr1_k(g__k_)
            g__exp_ = x
            g__pc_ = value_of_cps

        case union_t.expr.zero:
            x = g__exp_.nexp
            g__k_ = kt_make_zero_k(g__k_)
            g__exp_ = x
            g__pc_ = value_of_cps

        case union_t.expr.mult:
            xr1 = g__exp_.nexpr1
            xr2 = g__exp_.nexpr2
            g__k_ = kt_make_mult_outer_k(xr2, g__env_cps_, g__k_)
            g__exp_ = xr1
            g__pc_ = value_of_cps

        case union_t.expr._if:
            test = g__exp_.test
            conseq = g__exp_.conseq
            alt = g__exp_.alt
            g__k_ = kt_make_if_k(conseq, alt, g__env_cps_, g__k_)
            g__exp_ = test
            g__pc_ = value_of_cps

        case union_t.expr.letcc:
            body = g__exp_.body
            g__exp_ = body
            g__env_cps_ = envr_extend_env(g__env_cps_, g__k_)
            g__pc_ = value_of_cps

        case union_t.expr.throw:
            k_exp = g__exp_.kexp
            v_exp = g__exp_.vexp
            g__k_ = kt_make_throw_k(v_exp, g__env_cps_)
            g__exp_ = k_exp
            g__pc_ = value_of_cps

        case union_t.expr.let:
            e = g__exp_.exp
            body = g__exp_.body
            g__k_ = kt_make_let_k(body, g__env_cps_, g__k_)
            g__exp_ = e
            g__pc_ = value_of_cps

        case union_t.expr._lambda:
            body = g__exp_.body
            g__v_ = clos_make(body, g__env_cps_, g__k_)
            g__pc_ = apply_k

        case union_t.expr.app:
            rator = g__exp_.rator
            rand = g__exp_.rand
            g__k_ = kt_make_rator_k(rand, g__env_cps_, g__k_)
            g__exp_ = rator
            g__pc_ = value_of_cps

def mount_tram():
    global g__pc_
    global g__k_
    global _dismount_blank
    g__k_= kt_empty_k(_dismount_blank)

    while True:
        greenlet(g__pc_).switch()


def racket_printf(s, *args):
    import re
    print(re.sub(r"~[a-z]", lambda x: "{}", s).format(*args))

if __name__ == '__main__':
    def _blank():
        pass
    jump_mount_tram = greenlet(mount_tram)
    _dismount_blank = greenlet(_blank)
    g__exp_ = expr_let(    expr_lambda(    expr_lambda(    expr_if(    expr_zero(    expr_var(0)),     expr_const(1),     expr_mult(    expr_var(0),     expr_app(    expr_app(    expr_var(1),     expr_var(1)),     expr_subr1(    expr_var(0))))))),     expr_mult(    expr_letcc(    expr_app(    expr_app(    expr_var(1),     expr_var(1)),     expr_throw(    expr_var(0),     expr_app(    expr_app(    expr_var(1),     expr_var(1)),     expr_const(4))))),     expr_const(5)))
    g__env_cps_ = envr_empty_env()
    g__pc_ = value_of_cps
    jump_mount_tram.switch()
    racket_printf("Value of the function is ~a\n", g__v_)
