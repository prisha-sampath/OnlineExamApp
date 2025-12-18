<%
    session.removeAttribute("score");
    session.removeAttribute("quizType");
    session.removeAttribute("answerKey");
    response.sendRedirect("selectLanguage.jsp");
%>
