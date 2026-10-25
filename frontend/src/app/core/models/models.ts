export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  email: string;
  nombreCompleto: string;
  diasVacacionesDisponibles: number;
  roles: string[];
}

export interface Solicitud {
  id: number;
  empleadoId: number;
  empleadoNombre: string;
  empleadoEmail: string;
  tipoSolicitudCodigo: string;
  tipoSolicitudNombre: string;
  fechaInicio: string;
  fechaFin?: string;
  diasSolicitados: number;
  motivo: string;
  montoReembolso?: number;
  estado: 'PENDIENTE' | 'APROBADA_JEFE' | 'APROBADA_RRHH' | 'RECHAZADA';
  aprobadorNombre?: string;
  observacionAprobador?: string;
  fechaCreacion: string;
}

export interface CrearSolicitudRequest {
  codigoTipoSolicitud: string;
  fechaInicio: string;
  fechaFin?: string;
  diasSolicitados: number;
  motivo: string;
  montoReembolso?: number;
}

export interface DecisionSolicitudRequest {
  aprobada: boolean;
  observacion: string;
}

export interface ChatResponse {
  respuesta: string;
  tieneContextoPoliticas: boolean;
}

export interface ClasificacionResponse {
  tipoSolicitud: string;
  nivelUrgencia: 'BAJA' | 'MEDIA' | 'ALTA';
  responsableAsignado: string;
  justificacionBreve: string;
}

export interface AuditoriaLog {
  id: number;
  usuarioEmail: string;
  metodoHttp: string;
  uriAccedida: string;
  ipOrigen: string;
  codigoEstadoHttp: number;
  timestamp: string;
  metadata: string;
}

export interface PoliticaEmpresa {
  id: number;
  titulo: string;
  categoria: string;
  contenido: string;
  activo: boolean;
  version: number;
}

