create table if not exists public.classroom_favorites
(
    id            bigint,
    created_at    timestamp(6),
    updated_at    timestamp(6),
    class_room_id bigint,
    user_id       bigint
);

create table if not exists public.equipment_favorites
(
    id           bigint,
    created_at   timestamp(6),
    updated_at   timestamp(6),
    equipment_id bigint,
    user_id      bigint
);

create table if not exists public.equipment_rentals
(
    quantity      integer      not null,
    created_at    timestamp(6),
    equipment_id  bigint       not null,
    id            bigserial
        primary key,
    rental_time   timestamp(6) not null,
    return_time   timestamp(6) not null,
    updated_at    timestamp(6),
    user_id       bigint       not null,
    rental_status varchar(255) not null
        constraint equipment_rentals_rental_status_check
            check ((rental_status)::text = ANY
                   ((ARRAY ['AVAILABLE'::character varying, 'IN_USE'::character varying, 'BROKEN'::character varying, 'RENTAL_PENDING'::character varying, 'RETURN_PENDING'::character varying])::text[]))
);

create table if not exists public.equipment_broken_history
(
    broken_by_id   bigint,
    created_at     timestamp(6),
    equipment_id   bigint       not null,
    id             bigserial
        primary key,
    updated_at     timestamp(6),
    broken_detail  varchar(500),
    broken_by_name varchar(255) not null,
    broken_type    varchar(255) not null
        constraint equipment_broken_history_broken_type_check
            check ((broken_type)::text = ANY
                   ((ARRAY ['RETURN_BROKEN'::character varying, 'ADMIN_BROKEN'::character varying])::text[]))
);

create table if not exists public.equipment_repair_history
(
    broken_history_id bigint,
    created_at        timestamp(6),
    equipment_id      bigint not null,
    id                bigserial
        primary key,
    updated_at        timestamp(6),
    repair_detail     varchar(500)
);

create table if not exists public.categories
(
    created_at timestamp(6),
    id         bigserial
        primary key,
    updated_at timestamp(6),
    name       varchar(255) not null
        unique
);

create table if not exists public.community
(
    recommend         integer,
    view              integer,
    author_id         bigint,
    board_category_id bigint,
    created_at        timestamp(6),
    id                bigserial
        primary key,
    updated_at        timestamp(6),
    attachment_url    varchar(255),
    content           text         not null,
    nickname          varchar(255) not null,
    title             varchar(255) not null
);

create table if not exists public.board
(
    recommend         integer,
    view              integer,
    author_id         bigint,
    board_category_id bigint,
    created_at        timestamp(6),
    deleted_at        timestamp(6),
    id                bigserial
        primary key,
    updated_at        timestamp(6),
    attachment_url    varchar(255),
    content           text         not null,
    nickname          varchar(255) not null,
    title             varchar(255) not null
);

create table if not exists public.board_category
(
    created_at  timestamp(6),
    deleted_at  timestamp(6),
    id          bigserial
        primary key,
    updated_at  timestamp(6),
    description varchar(255) not null,
    name        varchar(255) not null
        unique
);

create table if not exists public.broken_repair_history
(
    broken_by_id             bigint,
    classroom_id             bigint,
    created_at               timestamp(6),
    deleted_at               timestamp(6),
    equipment_id             bigint,
    id                       bigserial
        primary key,
    related_broken_id        bigint,
    updated_at               timestamp(6),
    broken_by_name           varchar(255),
    broken_by_student_number varchar(255),
    broken_type              varchar(255)
        constraint broken_repair_history_broken_type_check
            check ((broken_type)::text = ANY
                   ((ARRAY ['RETURN_BROKEN'::character varying, 'ADMIN_BROKEN'::character varying])::text[])),
    detail                   varchar(255),
    history_type             varchar(255) not null
        constraint broken_repair_history_history_type_check
            check ((history_type)::text = ANY
                   ((ARRAY ['BROKEN'::character varying, 'REPAIR'::character varying])::text[])),
    target_type              varchar(255) not null
        constraint broken_repair_history_target_type_check
            check ((target_type)::text = ANY
                   ((ARRAY ['EQUIPMENT'::character varying, 'CLASSROOM'::character varying])::text[]))
);

create table if not exists public.classroom
(
    end_rent_time   time(6),
    end_time        time(6),
    start_rent_time time(6),
    start_time      time(6),
    created_at      timestamp(6),
    deleted_at      timestamp(6),
    id              bigserial
        primary key,
    manager_id      bigint,
    renter_id       bigint,
    requested_at    timestamp(6),
    updated_at      timestamp(6),
    description     varchar(255),
    image_url       varchar(255),
    name            varchar(255),
    status          varchar(255)
        constraint classroom_status_check
            check ((status)::text = ANY
                   ((ARRAY ['AVAILABLE'::character varying, 'IN_USE'::character varying, 'BROKEN'::character varying, 'RENTAL_PENDING'::character varying, 'RETURN_PENDING'::character varying])::text[]))
);

create index if not exists idx_deleted_at_classroom
    on public.classroom (deleted_at);

create table if not exists public.equipment
(
    broken_count               bigint       not null,
    category_id                bigint,
    created_at                 timestamp(6),
    deleted_at                 timestamp(6),
    end_rent_date              timestamp(6),
    id                         bigserial
        primary key,
    manager_id                 bigint,
    model_id                   bigint,
    rental_count               bigint       not null,
    renter_semester_schdule_id bigint,
    renter_user_id             bigint,
    repair_count               bigint       not null,
    requested_time             timestamp(6),
    start_rent_date            timestamp(6),
    updated_at                 timestamp(6),
    description                text,
    image_url                  varchar(255),
    restriction_grade          varchar(255),
    serial_number              varchar(255)
        unique,
    status                     varchar(255) not null
        constraint equipment_status_check
            check ((status)::text = ANY
                   ((ARRAY ['AVAILABLE'::character varying, 'IN_USE'::character varying, 'BROKEN'::character varying, 'RENTAL_PENDING'::character varying, 'RETURN_PENDING'::character varying])::text[]))
);

create index if not exists idx_deleted_at_equipment
    on public.equipment (deleted_at);

create table if not exists public.equipment_carts
(
    created_at   timestamp(6),
    deleted_at   timestamp(6),
    equipment_id bigint not null,
    id           bigserial
        primary key,
    updated_at   timestamp(6),
    user_id      bigint not null
);

create table if not exists public.equipment_category
(
    max_rental_count integer      not null,
    created_at       timestamp(6),
    deleted_at       timestamp(6),
    id               bigserial
        primary key,
    updated_at       timestamp(6),
    english_code     varchar(255) not null,
    name             varchar(255) not null
);

create table if not exists public.equipment_model
(
    available         boolean not null,
    model_group_index integer,
    category_id       bigint,
    created_at        timestamp(6),
    deleted_at        timestamp(6),
    id                bigserial
        primary key,
    updated_at        timestamp(6),
    english_code      varchar(255),
    name              varchar(255)
);

create table if not exists public.inquiry
(
    author_id      bigint,
    created_at     timestamp(6),
    deleted_at     timestamp(6),
    id             bigserial
        primary key,
    updated_at     timestamp(6),
    attachment_url varchar(255),
    content        varchar(255) not null,
    status         varchar(255) not null
        constraint inquiry_status_check
            check ((status)::text = ANY
                   ((ARRAY ['NOT_ANSWERED'::character varying, 'ANSWERED'::character varying])::text[])),
    title          varchar(255) not null,
    type           varchar(255) not null
        constraint inquiry_type_check
            check ((type)::text = ANY
                   ((ARRAY ['RENTAL'::character varying, 'RETURN'::character varying, 'SYSTEM_ERROR'::character varying, 'EQUIPMENT_BROKEN'::character varying, 'PENALTY'::character varying, 'ETC'::character varying])::text[]))
);

create table if not exists public.inquiry_answer
(
    created_at   timestamp(6),
    deleted_at   timestamp(6),
    id           bigserial
        primary key,
    inquiry_id   bigint,
    responder_id bigint,
    updated_at   timestamp(6),
    content      varchar(1000) not null
);

create table if not exists public.notice
(
    important      boolean      not null,
    view           integer      not null,
    author_id      bigint,
    created_at     timestamp(6),
    deleted_at     timestamp(6),
    id             bigserial
        primary key,
    updated_at     timestamp(6),
    attachment_url varchar(255),
    content        text         not null,
    title          varchar(255) not null
);

create table if not exists public.notifications
(
    read       boolean not null,
    created_at timestamp(6),
    deleted_at timestamp(6),
    id         bigserial
        primary key,
    updated_at timestamp(6),
    user_id    bigint,
    category   varchar(255),
    link       varchar(255),
    message    varchar(255),
    title      varchar(255)
);

create table if not exists public.professor
(
    created_at  timestamp(6),
    deleted_at  timestamp(6),
    id          bigserial
        primary key,
    updated_at  timestamp(6),
    description varchar(255) not null,
    name        varchar(255) not null
        unique
);

create index if not exists idx_deleted_at_professor
    on public.professor (deleted_at);

create table if not exists public.refresh_token
(
    expires_at bigint,
    id         bigserial
        primary key,
    key        varchar(255) not null
        unique,
    value      varchar(255) not null
);

create table if not exists public.rental_history
(
    status            smallint
        constraint rental_history_status_check
            check ((status >= 0) AND (status <= 6)),
    broken_history_id bigint,
    classroom_id      bigint,
    created_at        timestamp(6),
    deleted_at        timestamp(6),
    equipment_id      bigint,
    id                bigserial
        primary key,
    renter_user_id    bigint,
    repair_history_id bigint,
    updated_at        timestamp(6),
    reason            varchar(255),
    target_type       varchar(255) not null
        constraint rental_history_target_type_check
            check ((target_type)::text = ANY
                   ((ARRAY ['EQUIPMENT'::character varying, 'CLASSROOM'::character varying])::text[]))
);

create table if not exists public.rental_restriction
(
    created_at timestamp(6),
    deleted_at timestamp(6),
    end_at     timestamp(6) not null,
    id         bigserial
        primary key,
    updated_at timestamp(6),
    user_id    bigint       not null,
    reason     varchar(255) not null
        constraint rental_restriction_reason_check
            check ((reason)::text = ANY
                   ((ARRAY ['OVERDUE'::character varying, 'BROKEN'::character varying, 'LOST'::character varying])::text[])),
    type       varchar(255) not null
        constraint rental_restriction_type_check
            check ((type)::text = ANY ((ARRAY ['EQUIPMENT'::character varying, 'CLASSROOM'::character varying])::text[]))
);

create table if not exists public.report_type
(
    created_at  timestamp(6),
    deleted_at  timestamp(6),
    id          bigserial
        primary key,
    updated_at  timestamp(6),
    description varchar(255) not null,
    name        varchar(255) not null
        unique
);

create table if not exists public.semester
(
    end_date   date,
    start_date date,
    year       integer,
    created_at timestamp(6),
    deleted_at timestamp(6),
    id         bigserial
        primary key,
    updated_at timestamp(6),
    name       varchar(20)
);

create index if not exists idx_deleted_at_semester
    on public.semester (deleted_at);

create table if not exists public.semester_schedule
(
    day          integer,
    end_at       time(6),
    start_at     time(6),
    classroom_id bigint,
    created_at   timestamp(6),
    deleted_at   timestamp(6),
    id           bigserial
        primary key,
    professor_id bigint,
    semester_id  bigint,
    updated_at   timestamp(6),
    name         varchar(20),
    color        varchar(255)
);

create index if not exists idx_deleted_at_semester_schedule
    on public.semester_schedule (deleted_at);

create table if not exists public.users
(
    birth_date        date         not null,
    broken_count      integer,
    grade             integer      not null,
    rental_count      integer,
    report_count      integer,
    restriction_count integer,
    created_at        timestamp(6),
    deleted_at        timestamp(6),
    professor_id      bigint,
    updated_at        timestamp(6),
    user_id           bigserial
        primary key,
    department        varchar(255),
    email             varchar(255),
    gender            varchar(255) not null
        constraint users_gender_check
            check ((gender)::text = ANY ((ARRAY ['M'::character varying, 'F'::character varying])::text[])),
    "group"           varchar(255),
    major             varchar(255),
    name              varchar(255) not null,
    nickname          varchar(255) not null,
    password          varchar(255),
    phone_number      varchar(255) not null,
    profile_picture   varchar(255),
    role              varchar(255) not null
        constraint users_role_check
            check ((role)::text = ANY
                   ((ARRAY ['ROLE_USER'::character varying, 'ROLE_ADMIN'::character varying, 'ROLE_SUPER_ADMIN'::character varying])::text[])),
    student_number    varchar(255)
);

create table if not exists public.comment
(
    author_id         bigint
        constraint fkir20vhrx08eh4itgpbfxip0s1
            references public.users,
    created_at        timestamp(6),
    deleted_at        timestamp(6),
    id                bigserial
        primary key,
    parent_comment_id bigint
        constraint fkhvh0e2ybgg16bpu229a5teje7
            references public.comment,
    target_id         bigint       not null,
    updated_at        timestamp(6),
    content           varchar(255) not null,
    type              varchar(255)
        constraint comment_type_check
            check ((type)::text = ANY
                   ((ARRAY ['BOARD'::character varying, 'NOTICE'::character varying, 'INQUIRY'::character varying])::text[]))
);

create table if not exists public.recommendations
(
    community_id bigint not null
        constraint fkc7y661dbdxb4j64rj9x9hr7ps
            references public.board,
    created_at   timestamp(6),
    deleted_at   timestamp(6),
    id           bigserial
        primary key,
    updated_at   timestamp(6),
    user_id      bigint not null
        constraint fk3c9w1lipqdutm65a9inevwfp0
            references public.users
);

create table if not exists public.report
(
    author_id      bigint
        constraint fk2pt3igqrhqmh5jcld6i6epw5i
            references public.users,
    created_at     timestamp(6),
    deleted_at     timestamp(6),
    id             bigserial
        primary key,
    report_type_id bigint
        constraint fk989tfinp1jxv79fvmju0cfpj3
            references public.report_type,
    updated_at     timestamp(6),
    content        varchar(255) not null,
    title          varchar(255) not null
);

create index if not exists idx_deleted_at_user
    on public.users (deleted_at);

create table if not exists public.year_schedule
(
    date         date,
    end_at       time(6),
    is_holiday   boolean,
    start_at     time(6),
    classroom_id bigint,
    created_at   timestamp(6),
    deleted_at   timestamp(6),
    id           bigserial
        primary key,
    updated_at   timestamp(6),
    description  varchar(20),
    color        varchar(255)
);

create index if not exists idx_deleted_at_year_schedule
    on public.year_schedule (deleted_at);