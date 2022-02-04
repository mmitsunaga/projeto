CREATE OR REPLACE VIEW VIEW_USU_PERM_GRUPO_GERAL
AS SELECT U.ID_USU_SERV AS ID_USU_SERV,
    P.PERM_CODIGO      AS PERM_CODIGO,
    P.ID_PERM          AS ID_PERM,
    P.ID_PERM_ESPECIAL AS ID_PERM_ESPECIAL,
    Usg.Id_Grupo       As Id_Grupo
  From Usu_Serv U  
  Inner Join Usu_Serv_Grupo Usg  On U.Id_Usu_Serv = Usg.Id_Usu_Serv
  Join Grupo_Perm Gp  On Usg.Id_Grupo = Gp.Id_Grupo
  JOIN PERM P  ON P.ID_PERM          = GP.ID_PERM
  WHERE Usg.Ativo = 1