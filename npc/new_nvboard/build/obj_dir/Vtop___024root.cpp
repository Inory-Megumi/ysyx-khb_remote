// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Design implementation internals
// See Vtop.h for the primary calling header

#include "Vtop___024root.h"
#include "Vtop__Syms.h"

//==========

VL_INLINE_OPT void Vtop___024root___combo__TOP__1(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___combo__TOP__1\n"); );
    // Body
    vlSelf->led = ((0xfffcU & (IData)(vlSelf->led)) 
                   | (1U & (((IData)(vlSelf->sw) >> 1U) 
                            ^ (IData)(vlSelf->sw))));
}

VL_INLINE_OPT void Vtop___024root___sequent__TOP__3(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___sequent__TOP__3\n"); );
    // Variables
    SData/*13:0*/ __Vdly__top__DOT____Vcellout__t_light____pinNumber3;
    IData/*31:0*/ __Vdly__top__DOT__t_light__DOT__count;
    // Body
    __Vdly__top__DOT__t_light__DOT__count = vlSelf->top__DOT__t_light__DOT__count;
    __Vdly__top__DOT____Vcellout__t_light____pinNumber3 
        = vlSelf->top__DOT____Vcellout__t_light____pinNumber3;
    if (vlSelf->rst) {
        __Vdly__top__DOT____Vcellout__t_light____pinNumber3 = 1U;
        __Vdly__top__DOT__t_light__DOT__count = 0U;
    } else {
        if ((0U == vlSelf->top__DOT__t_light__DOT__count)) {
            __Vdly__top__DOT____Vcellout__t_light____pinNumber3 
                = ((0x3ffeU & ((IData)(vlSelf->top__DOT____Vcellout__t_light____pinNumber3) 
                               << 1U)) | (1U & ((IData)(vlSelf->top__DOT____Vcellout__t_light____pinNumber3) 
                                                >> 0xdU)));
        }
        __Vdly__top__DOT__t_light__DOT__count = ((0x7a120U 
                                                  <= vlSelf->top__DOT__t_light__DOT__count)
                                                  ? 0U
                                                  : 
                                                 ((IData)(1U) 
                                                  + vlSelf->top__DOT__t_light__DOT__count));
    }
    vlSelf->top__DOT__t_light__DOT__count = __Vdly__top__DOT__t_light__DOT__count;
    vlSelf->top__DOT____Vcellout__t_light____pinNumber3 
        = __Vdly__top__DOT____Vcellout__t_light____pinNumber3;
    vlSelf->led = ((3U & (IData)(vlSelf->led)) | ((IData)(vlSelf->top__DOT____Vcellout__t_light____pinNumber3) 
                                                  << 2U));
}

void Vtop___024root___eval(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___eval\n"); );
    // Body
    Vtop___024root___combo__TOP__1(vlSelf);
    if (((IData)(vlSelf->clk) & (~ (IData)(vlSelf->__Vclklast__TOP__clk)))) {
        Vtop___024root___sequent__TOP__3(vlSelf);
    }
    // Final
    vlSelf->__Vclklast__TOP__clk = vlSelf->clk;
}

QData Vtop___024root___change_request_1(Vtop___024root* vlSelf);

VL_INLINE_OPT QData Vtop___024root___change_request(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___change_request\n"); );
    // Body
    return (Vtop___024root___change_request_1(vlSelf));
}

VL_INLINE_OPT QData Vtop___024root___change_request_1(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___change_request_1\n"); );
    // Body
    // Change detection
    QData __req = false;  // Logically a bool
    return __req;
}

#ifdef VL_DEBUG
void Vtop___024root___eval_debug_assertions(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___eval_debug_assertions\n"); );
    // Body
    if (VL_UNLIKELY((vlSelf->clk & 0xfeU))) {
        Verilated::overWidthError("clk");}
    if (VL_UNLIKELY((vlSelf->rst & 0xfeU))) {
        Verilated::overWidthError("rst");}
    if (VL_UNLIKELY((vlSelf->sw & 0xfcU))) {
        Verilated::overWidthError("sw");}
}
#endif  // VL_DEBUG
